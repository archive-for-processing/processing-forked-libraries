This is a Utility library to execute Scala code from Processing on a standalone Spark instance . 

You can also import in Netbeans, and possible Eclipse, or any other IDE that can read Maven projects. 

### Installation 

You need to clone the repository: 
``` bash
git clone https://github.com/raissi/processing-spark-client.git
```
Then you go under **processing-spark-client** and execute:
``` bash
mvn install
```
After Maven finishes the build, you need to run the deployment script:

``` bash
sh createLibrary.sh 
```

This creates an archive file *SparkClient.tgz* under the folder *out*. Unzip the archive and copy its content in a folder *SparkClient* situated under the library folder of Processing.

