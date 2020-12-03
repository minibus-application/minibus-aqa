package org.minibus.aqa.main.core.pagefactory;

import org.apache.commons.lang3.StringUtils;
import org.minibus.aqa.main.core.pagefactory.elements.base.View;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.InvocationTargetException;

public class ViewHelper {

    public static By resolveFindBy(FindBy findBy) {
        if (findBy == null) {
            return null;
        } else if (StringUtils.isNotEmpty(findBy.id())) {
            return By.id(findBy.id());
        } else if (StringUtils.isNotEmpty(findBy.name())) {
            return By.name(findBy.name());
        } else if (StringUtils.isNotEmpty(findBy.xpath())) {
            return By.xpath(findBy.xpath());
        } else if (StringUtils.isNotEmpty(findBy.css())) {
            return By.cssSelector(findBy.css());
        } else if (StringUtils.isNotEmpty(findBy.className())) {
            return By.className(findBy.className());
        } else if (StringUtils.isNotEmpty(findBy.linkText())) {
            return By.linkText(findBy.linkText());
        } else if (StringUtils.isNotEmpty(findBy.partialLinkText())) {
            return By.partialLinkText(findBy.partialLinkText());
        } else if (StringUtils.isNotEmpty(findBy.tagName())) {
            return By.tagName(findBy.tagName());
        }

        throw new IllegalArgumentException("FindBy could not be mapped to By: " + findBy.toString());
    }
}
