# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* entity_name = `Entity`
* dto_name = `EntityDto`
* property_name = `property`
* property_type = `type`

## Task Description
Your task is to implement a new parameter on a specific entity and its corresponding DTO. You need to add a flyway migration 
script defining the new column in the database, then update the entity and DTO classes to include the new property. Update
any needed tests and verify that they pass. Finally, update the OpenAPI documentation to reflect the changes made to the 
entity and DTO and verify the documentation with the linting tool. Use guidelines from the file `.junie/guidelines.md`.

### Task Steps
* Write a flyway migration script to add a new column `${property_name}` of type `${property_type}` to the existing table for the entity `${entity_name}`.
* Inspect the existing entity `${entity_name}` and identify the existing properties.
* Add a new property `${property_name}` of type `${property_type}` to the entity `${entity_name}`.
* Inspect the existing DTO `${dto_name}` and identify the existing properties.
* Add a new property `${property_name}` of type `${property_type}` to the DTO `${dto_name}`.
* Update the unit tests for the entity and DTO to cover the new property.
* Validate the updated unit tests are passing and fix any issues if they don't.
* Update the OpenAPI documentation to reflect the changes made to the entity and DTO.
* Update the parameters of the OpenAPI documentation to include the new property `${property_name}`.
* Verify the OpenAPI documentation is valid using the linting tool.