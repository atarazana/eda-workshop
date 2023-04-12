#!/bin/bash

# This function creates a namespace
function create_namespace() {
  local namespace="$1"

  # Check if the project exists
  if oc get project $namespace > /dev/null 2>&1; then
    echo "Project $namespace already exists"
  else
    # Create the project
    oc new-project $namespace
    echo "Project $namespace created"
  fi
}

# This function checks if the operator with the given name is ready in the given namespace
function check_operator_ready() {
  local operator_name="$1"
  local namespace="$2"

  # Set a timeout of 10 minutes (600 seconds)
  local timeout=600
  local start_time=$(date +%s)

  local csv_status=$(oc get csv -n $namespace -o jsonpath='{range .items[?(@.spec.displayName == "'"$operator_name"'")]}{.status.phase}{end}')

  while [ "$csv_status" != "Succeeded" ]; do
    local current_time=$(date +%s)
    local elapsed_time=$((current_time - start_time))

    # If the elapsed time exceeds the timeout, exit with an error
    if [ $elapsed_time -gt $timeout ]; then
      echo "ERROR: Timeout waiting for operator $operator_name to become ready"
      exit 1
    fi

    echo "Operator $operator_name is not ready. Waiting..."
    sleep 10
    csv_status=$(oc get csv -n $namespace -o jsonpath='{range .items[?(@.spec.displayName == "'"$operator_name"'")]}{.status.phase}{end}')
  done

  echo "Operator $operator_name is ready"
}

# This function checks if the Prometheus and Grafana objects created by the given CRD are ready
function check_monitoring_stack_ready() {
  local prometheus_name="$1"
  local grafana_name="$2"
  local namespace="$3"

  # Set a timeout of 5 minutes (300 seconds)
  local timeout=300
  local start_time=$(date +%s)

  # Check if the Prometheus and Grafana objects are ready
  local prometheus_ready=false
  local grafana_ready=false

  while ! $prometheus_ready || ! $grafana_ready; do
    local current_time=$(date +%s)
    local elapsed_time=$((current_time - start_time))

    # If the elapsed time exceeds the timeout, exit with an error
    if [ $elapsed_time -gt $timeout ]; then
      echo "ERROR: Timeout waiting for Prometheus and Grafana objects to become ready"
      exit 1
    fi

    # Check if the Prometheus and Grafana objects are ready
    local prometheus_status=$(oc get prometheus $prometheus_name -n $namespace -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')
    local grafana_status=$(oc get grafanas.integreatly.org $grafana_name -n $namespace -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')

    if [ "$prometheus_status" == "True" ]; then
      prometheus_ready=true
      echo "Prometheus object $prometheus_name is ready"
    else
      echo "Prometheus object $prometheus_name is not ready. Waiting..."
    fi

    if [ "$grafana_status" == "True" ]; then
      grafana_ready=true
      echo "Grafana object $grafana_name is ready"
    else
      echo "Grafana object $grafana_name is not ready. Waiting..."
    fi

    if ! $prometheus_ready || ! $grafana_ready; then
      sleep 10
    fi
  done

  echo "Prometheus and Grafana objects are ready"
}

# This function checks if the StatefulSet with the given name is ready
function check_statefulset_ready() {
  local statefulset_name="$1"
  local namespace="$2"

  # Set a timeout of 5 minutes (300 seconds)
  local timeout=300
  local start_time=$(date +%s)

  # Check if the StatefulSet is ready
  local statefulset_ready=false

  while ! $statefulset_ready; do
    local current_time=$(date +%s)
    local elapsed_time=$((current_time - start_time))

    # If the elapsed time exceeds the timeout, exit with an error
    if [ $elapsed_time -gt $timeout ]; then
      echo "ERROR: Timeout waiting for StatefulSet $statefulset_name to become ready"
      exit 1
    fi

    # Check if the StatefulSet is ready
    local replicas=$(oc get statefulset $statefulset_name -n $namespace -o jsonpath='{.status.readyReplicas}')
    local desired_replicas=$(oc get statefulset $statefulset_name -n $namespace -o jsonpath='{.spec.replicas}')

    if [ "$replicas" == "$desired_replicas" ]; then
      statefulset_ready=true
      echo "StatefulSet $statefulset_name is ready"
    else
      echo "StatefulSet $statefulset_name is not ready. Waiting..."
    fi

    if ! $statefulset_ready; then
      sleep 10
    fi
  done

  echo "StatefulSet $statefulset_name is ready"
}

# This function checks if the Deployment with the given name is ready
function check_deployment_ready() {
  local deployment_name="$1"
  local namespace="$2"

  # Set a timeout of 5 minutes (300 seconds)
  local timeout=300
  local start_time=$(date +%s)

  # Check if the Deployment is ready
  local deployment_ready=false

  while ! $deployment_ready; do
    local current_time=$(date +%s)
    local elapsed_time=$((current_time - start_time))

    # If the elapsed time exceeds the timeout, exit with an error
    if [ $elapsed_time -gt $timeout ]; then
      echo "ERROR: Timeout waiting for Deployment $deployment_name to become ready"
      exit 1
    fi

    # Check if the Deployment is ready
    local replicas=$(oc get deployment $deployment_name -n $namespace -o jsonpath='{.status.readyReplicas}')
    local desired_replicas=$(oc get deployment $deployment_name -n $namespace -o jsonpath='{.spec.replicas}')

    if [ "$replicas" == "$desired_replicas" ]; then
      deployment_ready=true
      echo "Deployment $deployment_name is ready"
    else
      echo "Deployment $deployment_name is not ready. Waiting..."
    fi

    if ! $deployment_ready; then
      sleep 10
    fi
  done

  echo "Deployment $deployment_name is ready"
}

create_build_config() {
    local build_config_name="$1"
    local folder_path="$2"
    local namespace="$3"

    oc get bc "$build_config_name" -n "$namespace" > /dev/null 2>&1
    if [[ $? -ne 0 ]]; then
        #oc new-build --binary=true --name="$build_config_name" -n "$namespace" -l="$labels"
        #oc patch bc "$build_config_name" --patch '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"./Dockerfile"}}}}' -n "$namespace"
        oc new-build $folder_path --name=$build_config_name
        oc start-build "$build_config_name" --from-dir="$folder_path" -n "$namespace"
    else
        echo "BuildConfig '$build_config_name' already exists in namespace '$namespace'."
    fi
}

# This function checks if the build with the given name is completed
function check_build_completed() {
  local bc_name="$1"
  local namespace="$2"
  local timeout="300"
  local interval=5

  echo "Checking if build for BuildConfig $bc_name is complete..."

  # Wait for the latest build of the BuildConfig to complete
  end=$((SECONDS+$timeout))
  while (( $SECONDS < $end )); do
    latest_build=$(oc get builds -n "$namespace" --sort-by='{.metadata.creationTimestamp}' -l buildconfig="$bc_name" -o jsonpath='{.items[-1:].metadata.name}')

    if [[ "$latest_build" == "" ]]; then
      echo "No builds found for BuildConfig $bc_name"
      return 1
    fi

    build_status=$(oc get builds "$latest_build" -n "$namespace" -o jsonpath='{.status.phase}')
    if [[ "$build_status" == "Complete" ]]; then
      echo "Build for BuildConfig $bc_name is complete"
      return 0
    else
      echo "Build for BuildConfig $bc_name is not yet complete"
    fi

    sleep $interval
  done

  echo "Timeout waiting for build for BuildConfig $bc_name to complete"
  return 1
}

function create_new_app() {
  local app_name="$1"
  local imagestream_name="$2"
  local namespace="$3"
  local env_vars="$4"
  local labels="$5"

  echo "Checking if app $app_name exists in namespace $namespace..."
  if oc get deployment "$app_name" -n "$namespace" &> /dev/null; then
    echo "App $app_name already exists"
  else
    echo "Creating app $app_name with imagestream $imagestream_name in namespace $namespace"
    oc new-app "$imagestream_name" --name="$app_name" -n "$namespace" $env_vars
    oc label deployment "$app_name" -n "$namespace" $labels
    echo "Waiting for app $app_name to be created"
    check_deployment_ready "$app_name" "$namespace"
  fi
}

function check_kafka_deployed() {
  local kafka_name="$1"
  local namespace="$2"
  local timeout="300"
  local interval=5

  echo "Checking if Kafka CR $kafka_name is deployed in namespace $namespace..."

  # Wait for the Kafka CR to be deployed
  end=$((SECONDS+$timeout))
  while (( $SECONDS < $end )); do
    if oc get kafka "$kafka_name" -n "$namespace" &> /dev/null; then
      kafka_status=$(oc get kafka "$kafka_name" -n "$namespace" -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')
      if [[ "$kafka_status" == "True" ]]; then
        echo "Kafka CR $kafka_name is deployed and ready"
        return 0
      else
        echo "Kafka CR $kafka_name is deployed but not yet ready"
      fi
    else
      echo "Kafka CR $kafka_name is not yet deployed"
    fi

    sleep $interval
  done

  echo "Timeout waiting for Kafka CR $kafka_name to be deployed"
  return 1
}

function check_kafkaconnect_deployed() {
  local kafkaconnect_name="$1"
  local namespace="$2"
  local timeout="300"
  local interval=5

  echo "Checking if KafkaConnect CR $kafkaconnect_name is deployed in namespace $namespace..."

  # Wait for the KafkaConnect CR to be deployed
  end=$((SECONDS+$timeout))
  while (( $SECONDS < $end )); do
    if oc get kafkaconnect "$kafkaconnect_name" -n "$namespace" &> /dev/null; then
      kafkaconnect_status=$(oc get kafkaconnect "$kafkaconnect_name" -n "$namespace" -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')
      if [[ "$kafkaconnect_status" == "True" ]]; then
        echo "KafkaConnect CR $kafkaconnect_name is deployed and ready"
        return 0
      else
        echo "KafkaConnect CR $kafkaconnect_name is deployed but not yet ready"
      fi
    else
      echo "KafkaConnect CR $kafkaconnect_name is not yet deployed"
    fi

    sleep $interval
  done

  echo "Timeout waiting for KafkaConnect CR $kafkaconnect_name to be deployed"
  return 1
}

check_knative_serving_ready() {
    local namespace="$1"

    # Check Knative Serving objects are ready
    local knative_ready
    knative_ready=$(oc get knativeserving/knative-serving -n "$namespace" -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')

    if [[ "$knative_ready" == "True" ]]; then
        echo "Knative Serving is ready in namespace '$namespace'."
        return 0
    fi

    echo "Waiting for Knative Serving to be ready in namespace '$namespace'..."
    local end_time=$((SECONDS+600))
    while [ $SECONDS -lt $end_time ]; do
        knative_ready=$(oc get knativeserving/knative-serving -n "$namespace" -o jsonpath='{.status.conditions[?(@.type=="Ready")].status}')

        if [[ "$knative_ready" == "True" ]]; then
            echo "Knative Serving is ready in namespace '$namespace'."
            return 0
        fi

        sleep 10
    done

    echo "Timeout waiting for Knative Serving to be ready in namespace '$namespace'."
    return 1
}

function check_knative_eventing_ready() {
    local namespace="${1:-knative-eventing}"
    local timeout="${2:-300}"  # default timeout is 300 seconds
    local interval=10          # check every 10 seconds
    local elapsed=0

    while true; do
        local output=$(oc get knativeeventing.operator.knative.dev/knative-eventing -n "$namespace" --template='{{range .status.conditions}}{{printf "%s=%s\n" .type .status}}{{end}}')
        local ready=$(echo "$output" | grep "Ready=True")

        if [ -n "$ready" ]; then
            echo "Knative Eventing objects are ready"
            return 0
        fi

        sleep "$interval"
        elapsed=$((elapsed+interval))

        if [ "$elapsed" -ge "$timeout" ]; then
            echo "Timeout: Knative Eventing objects are not ready after $timeout seconds"
            return 1
        fi
    done
}
