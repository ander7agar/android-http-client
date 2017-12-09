Android HTTP Client
===================

[![](https://jitpack.io/v/ander7agar/android-org.apache.http-client.svg)](https://jitpack.io/#ander7agar/android-org.apache.http-client)

Android Http Client is a small library to make requests to any internet service simple and practical way.
You can implement multiple interfaces for the management of the responses. It also includes interfaces
for managing upload and download of files.

These are the interfaces that includes the library and its use:

- **RequestStateListener**: Capture the events of the start and end of the request.
- **ResponseListener**: Capture the response when making the request.
- **FileUploadListener**: Capture an upload of a file.
- **FileDownloadListener**: Capture an download of a file.


Compatibility:
--------------
- **Android SDK**: Android Http Client requires a minimum API Level 14
- **Marek Sebera Http Client**: [Marek Sebera Http Client](https://github.com/smarek/httpclient-android) for Android.
- **JSON**: Android Http Client uses [JSON Java Library](http://www.json.org/) for compatibility json responses.
- **XML**: Android Http Client uses [Jsoup Library](https://jsoup.org) for compatibility xml responses.


How do I use this library:
--------------------------

```java
Request request = Request.create("http://service.server.com/getData");
request.setMethod("POST")
    .setTimeout(120) //2 Minutes
    .addHeader("Authorization", "Key=MY_SERVICE_KEY")
    .addParameter("key1", "value1")
    .addParameter("key2", "value3")
    .addParameter(new Parameter("key3", "value3"))
    .addParameter("file", new File("myfile.txt"))
    .setFileUploadListener(new FileUploadListener() {
        @Override
        public void onUploadingFile(File file, long size, long uploaded) {

        }
    })
    .setRequestStateListener(new RequestStateListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onUploadProgress(float progress) {
            
        }
                    
        @Override
        public void onConnectionError(Exception e) {
           e.printStackTrace();
        }
    })
    .setResponseListener(new JsonResponseListener() {
        @Override
        public void onOkResponse(JSONObject jsonObject) throws JSONException {

        }

        @Override
        public void onErrorResponse(JSONObject jsonObject) throws JSONException {

        }

        @Override
        public void onParseError(JSONException e) {

        }
    }).execute();
 ```

Download:
---------

**Gradle**:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.ander7agar:android-org.apache.http-client:0.4.4'
}
```

**Maven**:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
	</repository>
</repositories>
<dependency>
    <groupId>com.github.ander7agar</groupId>
    <artifactId>android-org.apache.http-client</artifactId>
    <version>0.4.4</version>
</dependency>
```

License:
-------

```
Copyright 2017 Andersson G. Acosta de la Rosa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    org.apache.http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```