**Android HTTP Client**

Example:

         `Request request = Request.create("http://service.server.com/getData");
         request.addHeader("Authorization", "Key=MY_SERVICE_KEY")
                 .addParameter("key1", "value1")
                 .addParameter("key2", "value3")
                 .addParameter(new Parameter("key3", "value3"))
                 .addParameter("file", new File(""))
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
                 }).execute();`

