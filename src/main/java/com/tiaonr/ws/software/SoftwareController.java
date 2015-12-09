package com.tiaonr.ws.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by echyong on 12/8/15.
 */
@Controller
public class SoftwareController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareController.class);

    @RequestMapping(value = "/api/checkupdate", method = RequestMethod.GET)
    @ResponseBody
    public SoftwareVersion checkUpdate(@RequestParam(value = "device", required = true) String device) {
        if ( LOGGER.isDebugEnabled() ) LOGGER.debug("check app version update for: " + device);
        SoftwareVersion ver = SoftwareVersions.get(device);
        if ( ver == null ) {
            ver = new SoftwareVersion(device);
        }
        if ( LOGGER.isDebugEnabled() )
            LOGGER.debug(ver.getDeviceType() + "\n" +
                    ver.getVersionCode() + "\n" +
                    ver.getVersionName() + "\n" +
                    ver.getDescription() + "\n" +
                    ver.getLocation());
        return ver;
    }
}
