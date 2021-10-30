package com.springrestsecuritycoreboilerplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private PasswordPolicy passwordPolicy;

    public PasswordPolicy getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    public static class PasswordPolicy {

        private Constraint constraint;
        private Integer minlength;
        private Integer maxlength;

        public Constraint getConstraint() {
            return constraint;
        }

        public void setConstraint(Constraint constraint) {
            this.constraint = constraint;
        }

        public Integer getMinlength() {
            return minlength;
        }

        public void setMinlength(Integer minlength) {
            this.minlength = minlength;
        }

        public Integer getMaxlength() {
            return maxlength;
        }

        public void setMaxlength(Integer maxlength) {
            this.maxlength = maxlength;
        }

        public static class Constraint {

            private Integer digit;
            private Integer uppercase;
            private Integer lowercase;
            private Integer specialcharacter;

            public Integer getDigit() {
                return digit;
            }

            public void setDigit(Integer digit) {
                this.digit = digit;
            }

            public Integer getUppercase() {
                return uppercase;
            }

            public void setUppercase(Integer uppercase) {
                this.uppercase = uppercase;
            }

            public Integer getLowercase() {
                return lowercase;
            }

            public void setLowercase(Integer lowercase) {
                this.lowercase = lowercase;
            }

            public Integer getSpecialcharacter() {
                return specialcharacter;
            }

            public void setSpecialcharacter(Integer specialcharacter) {
                this.specialcharacter = specialcharacter;
            }
        }
    }
}
