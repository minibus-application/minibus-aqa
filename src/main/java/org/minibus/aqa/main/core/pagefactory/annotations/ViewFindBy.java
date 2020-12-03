package org.minibus.aqa.main.core.pagefactory.annotations;

import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ViewFindBy {

    String name();

    FindBy findBy() default @FindBy();
}
