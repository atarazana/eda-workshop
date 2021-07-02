
# Switch to this database
USE enterprise;

#
# Set of queries to get check data
#

# Account statuses by Region

select region_code,status,count(*) from accounts a group by region_code,status;

# Account Active by Region

select region_code,count(*) from accounts where status = 'ACTIVE' group by region_code,status;

# Account Inactive by Region

select region_code,count(*) from accounts where status = 'INACTIVE' group by region_code,status;

# Account Closed by Region

select region_code,count(*) from accounts where status = 'CLOSED' group by region_code,status;

# Balance by Region

select region_code,sum(quantity) from accounts a, movements m where a.id = m.account_id group by a.region_code order by sum(quantity) desc;
