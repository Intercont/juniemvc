# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* controller_name = `BeerController`
* entity_name = `Beer`

## Task Description
Your task is to implement a new PATH operation into `${controller_name}`. 
Since this is a PATCH operation, it should be used to update an existing resource. 
The operation should accept a DTO that contains the properties to be updated, 
so create a new DTO class called `${entity_name}`PathDto containing the same properties as the entity without any constraints 
and ignore the properties createdDate and updatedDate. Adjust the existing service layer to support the new PATCH operation.
The service layer needs to retrieve the existing entity from the database first, then apply the changes from the non-null values from the DTO to the entity.
To update the entity, create a new mapper method in the existing mapper class to convert the `${entity_name}`PathDto to the `${entity_name}` entity.
Create all necessary unit tests for the controller, service implementation, mapper and repository to cover the new PATCH functionality. 
Make sure to verify that all tests are passing after the implementation and fix any issues if they don't.
Finally, update the OpenAPI documentation to reflect the changes made to the controller and verify the documentation with the linting tool.
Use guidelines from the file `.junie/guidelines.md`.