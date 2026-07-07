# Cloud Computing & AWS Reference Guide

## What is the Cloud?

Cloud computing is the on-demand delivery of IT resources over the internet with pay-as-you-go pricing. Instead of buying, owning, and maintaining physical data centers and servers, you access technology services, such as computing power, storage, and databases, from a provider like AWS.

### The Six Key Advantages
1.  **Trade Capital Expense (CapEx) for Variable Expense (OpEx):** Instead of investing heavily in data centers and servers before you know how you're going to use them, you pay only when you consume computing resources.
2.  **Benefit from Massive Economies of Scale:** Because AWS aggregates usage from hundreds of thousands of customers, they can achieve much lower costs, which are passed on to the user.
3.  **Stop Guessing Capacity:** Eliminate "idle hardware" by using autoscaling. You no longer have to worry about running out of capacity or over-paying for unused servers.
4.  **Increase Speed and Agility:** New IT resources are only a click away, reducing the time it takes to make those resources available to developers from weeks to minutes.
5.  **Stop Spending Money Running Data Centers:** Eliminate the "undifferentiated heavy lifting" of managing power, cooling, hardware maintenance, and physical security.
6.  **Go Global in Minutes:** Deploy applications in multiple regions around the world with a few clicks, providing lower latency and a better experience for your users.

### Cloud Deployment Models
*   **Public Cloud:** Services are owned and operated by a third-party provider (AWS, Azure, GCP) and delivered over the public internet. Resources are shared among multiple organizations (multi-tenancy).
*   **Private Cloud:** Cloud resources used exclusively by a single organization. It can be physically located on-site or hosted by a third party, providing higher control and security.
*   **Hybrid Cloud:** A combination of public and private clouds, allowing data and applications to be shared between them for greater flexibility.

### Cloud Service Models (The "As-A-Service" Stack)
*   **Infrastructure as a Service (IaaS):** Provides the fundamental building blocks: virtual servers, networking, and storage. You manage the Operating System (OS) and applications. 
    *   *Example: AWS EC2.*
*   **Platform as a Service (PaaS):** Removes the need for you to manage the underlying infrastructure (hardware and OS). You only manage the application and data.
    *   *Example: AWS RDS (Relational Database Service).*
*   **Software as a Service (SaaS):** A completed product managed by the provider. You simply use the software via a web browser.
    *   *Example: Salesforce, Gmail, or Slack.*

---

## AWS Introduction

Amazon Web Services (AWS) is the world’s most comprehensive and broadly adopted cloud platform.

### Global Infrastructure
To provide high availability and low latency, AWS is organized into a hierarchical structure:

*   **Regions:** A physical geographic location in the world (e.g., `us-east-1`) that contains multiple Availability Zones. Regions are isolated from one another to ensure data sovereignty and fault tolerance.
*   **Availability Zones (AZs):** One or more discrete data centers with redundant power, networking, and connectivity within a Region. AZs are connected via high-speed fiber. 
    *   *Best Practice:* Deploy applications across multiple AZs to ensure that if one data center fails, your app stays online.
*   **Edge Locations / Local Zones:**
    *   **Local Zones:** Place compute/storage closer to large population centers for ultra-low latency.
    *   **Wavelength Zones:** Places AWS services inside 5G networks for mobile edge computing.
    *   **Outposts:** Brings AWS hardware into your own physical data center.

### Identity and Access Management (IAM)
IAM is the framework used to manage **who** can access **what** in your AWS account.
*   **Users:** Individual people or applications.
*   **Groups:** Collections of users. It is a best practice to assign permissions to a **Group** and then add users to that group.
*   **Policies:** JSON documents that define permissions (e.g., "Allow User A to read from S3, but not delete").

> **Free Tier Warning:** Many AWS services have a "Free Tier." However, many services are interconnected. Always check the [AWS Free Tier page](https://aws.amazon.com/free/) and be careful with "Auto-scaling" settings (like RDS storage) that may exceed free limits automatically.

---

## AWS S3 (Simple Storage Service)

S3 is an **Object Storage** service. Unlike a computer hard drive, S3 stores data as "objects" in "buckets."

### Key Concepts
*   **Buckets:** The fundamental containers for data. Bucket names must be globally unique across all AWS users.
*   **Objects:** The files (images, videos, logs, HTML) stored in buckets. There is no limit to the total amount of data you can store in S3.
*   **Object Storage vs. Block Storage:** 
    *   **Object Storage (S3):** Best for unstructured data, files, and static web hosting. It is highly scalable and accessible via HTTP/URLs.
    *   **Block Storage (EBS):** Best for OS drives and databases. It acts like a hard drive attached to a specific server.

### Storage Classes (Cost Optimization)
Choosing the right class saves money based on how often you access data:
*   **Standard:** Frequent access; high performance.
*   **Intelligent-Tiering:** Automatically moves data to the cheapest tier based on usage patterns.
*   **Standard-Infrequent Access (IA):** Cheaper storage, but you pay a fee to retrieve data.
*   **Glacier (Instant, Flexible, or Deep Archive):** Extremely low-cost storage for long-term archives where retrieval might take minutes or hours.

### Access & Security
*   **Bucket Policies (Recommended):** Centralized rules applied to the entire bucket to manage access for many users at once.
*   **Access Control Lists (ACLs):** Older, more manual method of managing permissions for individual objects.
*   **Static Website Hosting:** S3 can be configured to serve HTML, CSS, and JS files directly to the internet, acting as a web server for static sites.

---

## AWS EC2 (Elastic Compute Cloud)

EC2 provides **virtual machines** (called "instances") in the cloud. This is the core of **IaaS**.

### Core Components
*   **AMI (Amazon Machine Image):** A pre-configured template containing the Operating System (Linux, Windows, etc.) and any pre-installed software. It allows you to launch new servers in seconds.
*   **Instance Types:** Different configurations of CPU, RAM, and Networking. You choose the "size" based on your application's needs.
*   **Security Groups:** A virtual firewall that controls traffic. You define rules based on **Protocol** (TCP/UDP), **Port** (e.g., 80 for HTTP, 22 for SSH), and **Source IP**.
*   **EBS (Elastic Block Store):** The "hard drive" for your EC2 instance. EBS volumes persist even if you stop or terminate the instance. You can take **Snapshots** (incremental backups) of EBS volumes to S3.

### Scaling & Availability
*   **Vertical Scaling:** Increasing the "size" of an existing instance (adding more RAM or CPU). Note: This usually requires a restart.
*   **Horizontal Scaling:** Adding **more instances** to your fleet. This is the preferred method for cloud applications.
*   **Auto Scaling Groups (ASG):** A service that automatically adds or removes EC2 instances based on demand (e.g., "If CPU usage > 70%, add 2 more servers").

### Accessing your Instance
*   **SSH (Secure Shell):** The standard protocol for remote access to Linux servers (Port 22).
*   **Key Pairs:** AWS uses Public Key Infrastructure. AWS holds the **Public Key**, and you must securely store the **Private Key** (`.pem` or `.ppk` file) on your local machine to gain access.

---

### AWS SDK (Software Development Kit)
The AWS SDK provides language-specific APIs that allow you to interact with AWS services programmatically rather than using the AWS Management Console or CLI.

*   **Purpose:** Enables developers to integrate AWS services directly into their application code (e.g., uploading a file to S3 or starting an EC2 instance automatically).
*   **Key Features:**
    *   **Language Support:** Available for popular languages such as Python (Boto3), JavaScript, Java, Go, and Ruby.
    *   **Authentication:** Automatically handles signing requests using your AWS credentials (Access Keys/Secret Keys).
    *   **Error Handling:** Provides built-in mechanisms to handle common network errors and service-side exceptions.
    *   **Retries:** Includes automated retry logic for transient errors to make applications more resilient.
*   **Workflow Example:**
    1.  Install the SDK for your preferred language.
    2.  Configure your credentials via the AWS CLI or environment variables.
    3.  Use the SDK's client objects to make API calls (e.g., `s3.put_object()`).

### AWS Command Line Interface (CLI)
The AWS CLI is a unified tool that allows you to manage your AWS services through your terminal or command prompt instead of using the web-based Management Console.

* **Purpose:** Enables automation of tasks, scripting, and quick service management via text commands.
* **Key Features:**
    * **Scripting & Automation:** Because it is text-based, you can write shell scripts (Bash, PowerShell) to automate repetitive infrastructure tasks.
    * **Consistency:** Provides a way to perform the same actions consistently across different environments.
    * **Configuration:** Uses a `credentials` file and a `config` file (typically located in your `~/.aws/` folder) to manage different user profiles and regions.
* **Core Concepts:**
    * **Commands:** Structured as `aws <service> <operation> <parameters>` (e.g., `aws s3 ls` to list S3 buckets).
    * **Output Formats:** Can be configured to output results in different formats, such as **JSON** (default), **text**, or **table**, which is highly useful for parsing data in scripts.
* **Security Tip:** When using the CLI, avoid hardcoding your Access Keys into scripts. Instead, use `aws configure` to set up local profiles or use IAM Roles for more secure access.

### AWS CLI Example Commands

**1. Spin up a Free Tier EC2 Instance (Ubuntu)**
To launch an instance, you use the `run-instances` command. Note that you will need to replace the placeholders (like `your-key-name` and `your-subnet-id`) with your actual values.

```bash
aws ec2 run-instances \
    --image-id ami-xxxxxxxxxxxxxxxxx \
    --count 1 \
    --instance-type t2.micro \
    --key-name your-key-name \
    --security-group-ids sg-xxxxxxxx \
    --subnet-id subnet-xxxxxxxx
```
*Note: The `--image-id` (AMI) must correspond to the specific Ubuntu AMI ID for your chosen AWS region.*

**2. Push files to an existing S3 bucket**
To upload a file, use the `s3 cp` (copy) command. To upload an entire folder, use the `s3 sync` command.

**Upload a single file:**
```bash
aws s3 cp myfile.txt s3://my-existing-bucket-name/
```

**Upload an entire directory (Sync):**
```bash
aws s3 sync ./my-local-folder s3://my-existing-bucket-name/
```