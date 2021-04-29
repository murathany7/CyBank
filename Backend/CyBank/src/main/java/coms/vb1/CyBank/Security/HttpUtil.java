package coms.vb1.CyBank.Security;

import coms.vb1.CyBank.Tables.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class HttpUtil{

    private HttpUtil(){

    }


    public static ResponseEntity<String> createResponse( String ret, int status ){

        return ResponseEntity.status(status).body(ret);

    }


}