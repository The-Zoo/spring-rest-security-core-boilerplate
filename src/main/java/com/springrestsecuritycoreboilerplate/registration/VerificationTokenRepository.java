package com.springrestsecuritycoreboilerplate.registration;

import org.springframework.data.repository.CrudRepository;


public interface VerificationTokenRepository  extends CrudRepository<VerificationToken, String>  {

}
