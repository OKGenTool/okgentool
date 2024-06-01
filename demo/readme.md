# Run Demo

This demo demonstrates the main features that are implemented.  
Bellow are the steps to setup and run a project using **OkGenTool**.

## Steps
1. Download this demo to a local folder on your computer. Example: `C:\ISEL\PS\okgentool\demo`  
1. Open a command line window in the same folder
1. Run the following command:
    ```
    java -jar okgentool.jar -s "C:\ISEL\PS\okgentool\demo\PetShop.yaml"
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

5. Observe the gen package, that contain the code that has been generated.
6. Observe the demo package, that contain the mutable code that has been implemented
for the propose of the demo.


In this demo part of the mutable code is already implemented, for a demonstration porpose,
but this is not the normal way to use our tool. The normal way is to generate the immutable
code using the OKGenTool and after that start the implementation of the mutable code using
the features that we offer. Despite we have implemented part of the mutable code we let one
endpoint to implement in this demo. Bellow are the steps to implement the endpoint GET
/users.
1. TODO
