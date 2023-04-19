
-- Switch to this database
USE enterprise;

--
-- Set of queries to get check data
--

-- Balance by Region
select '* Balance by Region *' AS '';
select region_code,sum(quantity) from accounts a, movements m where a.id = m.account_id group by a.region_code order by sum(quantity) desc;

-- Account Closed by Region
select '* Account Closed by Region *' AS '';
select region_code,count(*) from accounts where status = 'CLOSED' group by region_code,status;

-- Account Inactive by Region
select '* Account Inactive by Region *' AS '';
select region_code,count(*) from accounts where status = 'INACTIVE' group by region_code,status;

-- Account Active by Region
select '* Account Active by Region *' AS '';
select region_code,count(*) from accounts where status = 'ACTIVE' group by region_code,status;

-- Account statuses by Region
select '* Account statuses by Region *' AS '';
select region_code,status,count(*) from accounts a group by region_code,status;
