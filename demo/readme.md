# Pet Store Server Demo

This demo demonstrates the main features of **OKGenTool**.  
It's using a widely known OpenAPI Description (OAD), **[Pet Store](https://github.com/swagger-api/swagger-petstore/blob/master/src/main/resources/openapi.yaml)**, that contains several operations that a common API usually supports.
Bellow are the steps to setup and run the project **PetStoreServer** using **OKGenTool**.

### Before you begin
Before you begin, note that this demo takes in consideration some assumptions, and it's important to validate them before running the demo, to ensure that everything runs as expected. If these assumptions are not verified, a developer who wants to run the demo should make the necessary adjustments by it's own.  
The assumptions are:
- The demo is running on a Windows system
- IntelliJ is previously installed with kotlin compiler
- [`curl`](https://curl.se/) is installed on the running system 



## 1. Setup and generate code
1. Download this demo to a local folder on your computer. Example: `C:\ISEL\PS\okgentool\demo`  
1. Open a command line window in the same folder
1. Run the following command:
    ```
    java -jar okgentool.jar -s "C:\ISEL\PS\okgentool\demo\petstore.yaml"
    -ts "C:\ISEL\PS\okgentool\demo\PetStore\server\src\main\kotlin"
    -p "org.example.petstoreserver"
    ```
    Parameters:
    - `-s <sourcePath>` is the path for the OAD file
    - `-ts <targetPath>` is the target folder where the files will be generated
    - `-p <package>` (optional) is the base package name of your project.

>[!TIP]
 Adapt the path to your local system.  
 Use absolute paths only. Relative paths are in the roadmap here: https://github.com/OKGenTool/okgentool/issues/46
    

4. Open the demo folder with an IDE (IntelliJ recommended)  
>[!NOTE]
If IDE was previously opened, you may need to reopen it to reload the indexes, of the generated files.  
If the mutable code could not access the generatede code, and reopening the IDE, does not solves the problem, the IDE needs to be repaired. In IntelliJ use this option:  `File -> Repair IDE`

## 2. Observations
>[!CAUTION] TODO: review this chapter
1. Observe the gen package, that contain the code that has been generated.
1. Observe the demo package, that contain the mutable code that has been implemented
for the propose of the demo.


In this demo part of the mutable code is already implemented, for a demonstration porpose,
but this is not the normal way to use our tool. The normal way is to generate the immutable
code using the OKGenTool and after that start the implementation of the mutable code using
the features that we offer. Despite we have implemented part of the mutable code we let one
endpoint to implement in this demo. Bellow are the steps to implement the endpoint GET
/users.

## 3. Run the server
At this point you should have a error free running server.  
But the server is not handling requests yet.  
In the next chapter you will implement some operations.

## 4. Implement API Operations using the OKGenTool DSL
To implement the API Operations, edit the file `PetRoute.kt` in the package `routes`of the mutable code.  
This file has a variable `petServices` built previously for demo purposes, and it's intent is to simulate a **Services** module of a typical HTTP API server. This piece of code is mutable and of responsibility of the developer.

In this file, we will add an extension function `petDSLRouting` to the `OkGenDsl` generated class:
```kotlin
val petServices = PetServices()

fun OkGenDsl.petDSLRouting() {
    //...
}
```

### Add Pet
Let's implement the **Add Pet** operation.  
Inside the `petDSLRouting()`, when writing `route.` IDE will suggest the available operations. Choose `addPet`.

```kotlin
fun OkGenDsl.petDSLRouting() {
    route.addPet {
    }
}
```
Now we need to define the function that we need to pass to `addPet`. At this level we have available an object `request` that has properties passed in the HTTP request, either in the body, the query string or in the path. In this operation the property available is `pet`. Let's get the pet from the request and pass it to our internal services to add it to the database.
```kotlin
fun OkGenDsl.petDSLRouting() {
    route.addPet {
        val pet = request.pet
        val newPet = petServices.addPet(pet)
    }
}
```

We also need to implement a response.  
Other object available in all operations is the `response`. This object has all the responses defined in the OAD for this operation. In this case, we have two available responses:
- `addPetResponse200`
- `addPetResponse405`
  
It seems logical that the `addPetResponse200` should be used in a success operation and the `addPetResponse405` for a failed operation. Let's adapt our code accordingly.
>[!TIP]
It's recommended that you consult the OAD file, to get better context of the operation and to choose the best response from the ones defined in the description file.

```kotlin
fun OkGenDsl.petDSLRouting() {
    route.addPet {
        try {
            val pet = request.pet
            val newPet = petServices.addPet(pet)
            response.addPetResponse200(newPet)
        }catch (ex:Exception){
            response.addPetResponse405()
        }
    }
}
```


### Get Pet by ID
Next we will implement the operation **Get Pet by ID**.  
Like in the previous operation, the `request` object contains the property used to make the HTTP request. In this case the property is `petId`.
Like before, we should add the property in a local variable and use it to get the information from the internal services. Here are how the final code should look like:

```kotlin
fun OkGenDsl.petDSLRouting() {    
    //...
    route.getPetById {
        try {
            val petId = request.petId
            val pet = petServices.getPet(petId)
            if (pet != null)
                response.getPetByIdResponse200(pet) //successful operation 
            else
                response.getPetByIdResponse404() //Pet not found
        } catch (ex: Exception) {
            response.getPetByIdResponse400() //Invalid ID supplied
        }
    }
}
```

For this operation, the OAD defines the following responses with the respective descriptions:
- `getPetByIdResponse200` - successful operation
- `getPetByIdResponse400` - Invalid ID supplied
- `getPetByIdResponse404` - Pet not found


### Update Pet
# TODO

## 5. Test the server
Run the server and let's test the implemented operations.

### Add Pet
Accordingly to the OAD, the operation **Add Pet** consist in making a `POST` request to the path `/pet` passing an object in `json` or `xml` format with two required properties: `name` and `photoUrls`.  
Using curl the command should look like this:

```
curl -i -X POST http://127.0.0.1:8080/pet -H "Content-Type: application/json" -d "{\"name\": \"boby\", \"photoUrls\": [\"photoUrls\"]}"

```
If everything worked as expected, this should be the result:
```
HTTP/1.1 200 Successful operation
Content-Length: 90
Content-Type: application/json

{"id":1,"name":"boby","category":null,"photoUrls":["photoUrls"],"tags":null,"status":null}
```

If any error ocurred (example: the object could not be parsed due to incorrect format), the output will be:
```
HTTP/1.1 405 Invalid input
Content-Length: 0
```

### Get Pet by ID
The operation to get a pet providing a pet id, is realized through making a `GET` request to the path `/pet/{petId}`.

```
curl -i http://127.0.0.1:8080/pet/1 
```

Expected result if the pet is found:
```
HTTP/1.1 200 successful operation
Content-Length: 90
Content-Type: application/json

{"id":1,"name":"boby","category":null,"photoUrls":["photoUrls"],"tags":null,"status":null}
```

Expected result if the pet is not found:
```
HTTP/1.1 404 Pet not found
Content-Length: 0
```

Expected result if it was passed an invalid ID in the path parameter (example `/pet/xxx`):
```
HTTP/1.1 400 Invalid ID supplied
Content-Length: 0
```


### Update Pet
1. update pet
2. get pet again to confirm the modification made by the previous operation


## 4. Limitations
**OKGenTool** has some limitations that are identified and planned to be solved/implemented.  
The known limitations, by module,  are:
### DSL
1. Could not handle operations with multiple query strings or path parameters
2. Could not handle operations with combination of parameters in query string with path parameters and with bodies.

### Components