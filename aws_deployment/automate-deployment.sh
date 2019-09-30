# For macbook users to install python
# # Before installing Python, you’ll need to install GCC. GCC can be obtained by downloading x-code
# xcode-select --install
# # install homebrew
# ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
# brew install python

#install pip 
# python -m ensurepip --default-pip
# python -m pip install --upgrade pip setuptools wheel

# # install awscli and aws-saml-cli thru pip manager
# pip install awscli --upgrade --user
# pip install aws-sam-cli

# install jq
#brew install jq

#create s3 bucket and upload java application jar
echo 'Creating s3 bucket deployment-bilby-lambdas-...'
aws s3 mb s3://deployment-bilby-lambdas-2018 --region ap-southeast-2
echo 'Copying file to s3'
aws s3 cp CRServer-1.0-SNAPSHOT.jar s3://deployment-bilby-lambdas-2018/

# run cloudformation template
echo 'Creating stack...'
aws cloudformation create-stack --stack-name mynewstack --template-body file://bilby-aws-account-cloudformer.template --region=ap-southeast-2 
echo '==================   Stack creation COMPLETE        =========================='

#create role for lambda
echo 'creating role for lambda...'
aws iam create-role --role-name lambda_basic_execution --assume-role-policy-document file://permissionpolicy.json
echo 'attaching policies for lambda'
aws iam attach-role-policy --policy-arn arn:aws:iam::aws:policy/AmazonRDSFullAccess --role-name lambda_basic_execution
aws iam attach-role-policy --policy-arn arn:aws:iam::aws:policy/AmazonAPIGatewayInvokeFullAccess --role-name lambda_basic_execution
aws iam attach-role-policy --policy-arn arn:aws:iam::aws:policy/AmazonVPCFullAccess --role-name lambda_basic_execution
aws iam attach-role-policy --policy-arn  arn:aws:iam::aws:policy/service-role/AmazonAPIGatewayPushToCloudWatchLogs  --role-name lambda_basic_execution
echo '==========lambda_basic_execution role created successfully with policies attached.========='
lambdarole=$(aws iam get-role --role-name lambda_basic_execution | jq '."Role".Arn')
export lambdarole

#create cognito user pool
echo 'creating cognito user pool'
aws cognito-idp create-user-pool --pool-name bilby-app-pool
echo '===================bilby-app-pool user pool CREATED ===================='

# deploy new stack that creates all lambdas in new account
echo 'Deploying all lambdas'
sam deploy --template-file deploy_lambdas.yaml   --stack-name stack-all-lambdas
echo '======================== Lambdas all created =============================='
