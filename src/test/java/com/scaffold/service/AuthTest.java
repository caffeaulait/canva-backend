package com.scaffold.service;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class AuthTest {

    @Autowired
    private AuthService authService;

    @Test
    public void testVerifyToken() {
        //fetch your own token and replace here
        String token = "eyJraWQiOiJjYjkyMDY4ZS1iNTJhLTRhZjctYTgyYy0zYTEyMjVlZWM2NDciLCJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MDA3NDk1OTcsIm5iZiI6MTcwMDc0OTU5NywiZXhwIjoxNzAwNzQ5ODk3LCJhdWQiOiJBQUYwX1QxYjJuOCIsInVzZXJJZCI6IkFUNVlRSjVGcU9oNjhwT2JDMWUwaWJLMm1aaFktVnhDZllkZlhveDZXbTg9IiwiYnJhbmRJZCI6IkFUNVlRSjR2NlhjWVN4eWVEWkU0NXRLbFZLeElEZmNZd3M4VExSSGZpQnM9In0.an5mTEplzse7J4TT-JU1S0xaEe5muZZZJY-GNs3FW3JJJ0ZSbk0Hq32Jo47dZVAq_5RbmXesi6E72W8mAifVxrtFe4i0ui1o46ezlCuRINkf7U3UynHZiWfkhuC2mVzllVhH5VeW4GviHAs-lbrrcSq5F-gLfhMcs0ZJxi08hGWWtATRPw5jtNrpVJMYq-GFoH8F12mZjp2Hp1cuUWayv6xKbpPBU_3HeRcYwuMJJtLXu7VN4kii5YbLTiOENgW1ntq8hmFsf-9PBRosdui54WS9yImkPDfPdVI8ltTRvPYMVMcDYgaB3SKRMih42vRp7WoP3mT0JoSJtNuDIN-jlQ";
        Assert.assertTrue(authService.verifyToken(token));
    }

}
