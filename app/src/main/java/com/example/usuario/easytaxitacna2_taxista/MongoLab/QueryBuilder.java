package com.example.usuario.easytaxitacna2_taxista.MongoLab;

public class QueryBuilder {
    public String getDatabaseName() {
        return "usuarios";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey() {
        return "8qA8tnTcNUu88N8fovQ-M_5a2jy0p7rS";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Returns the docs101 collection
     * @return
     */
    public String documentRequest()
    {
        return "usuarios";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return
     */
    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }


    public String createContact(Usuario datouser)
    {
        return String.format("{\"document\"  : {\"nomb_user\": \"%s\", "
                        + "\"pass_user\": \"%s\", \"email_user\": \"%s\", "
                        + "\"tel_user\": \"%s\"}, \"safe\" : true}",
                datouser.nomb_user, datouser.pass_user, datouser.email_user,datouser.tel_user);
    }


}
