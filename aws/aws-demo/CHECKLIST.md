# Cloud Checklist

## Spring checks
- make sure your `server.address` property is set to 0.0.0.0
- make sure your CORS configs are properly set up

## Front end check
- make sure your web requests are being sent to the correct location

## In your virtual machine (Ubuntu for demo)
- update apt registry `sudo apt update -y`
- upgrade software `sudo apt upgrade -y`
- download java `sudo apt install openjdk-21-jdk`
    - NOTE: if you just need to run your JAR you can specify `jre` instead of `jdk`
- if building your JAR in the instance make gradlew executable with `sudo chmod +x gradlew`

## in your s3 bucket
- turn on static website hosting 
- set bucket resources to public
- add a policy allowing users to `GET` your website resources
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::{your bucket name here}/*"
        }
    ]
}
```