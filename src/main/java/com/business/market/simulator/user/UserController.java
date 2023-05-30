package com.business.market.simulator.user;

import com.business.market.simulator.utils.InputScanner;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Setter(onMethod_ = {@Autowired})
public class UserController {

    private UserAuthenticationService userAuthenticationService;

    private InputScanner scanner;

}
