package org.minibus.aqa.main.core.pagefactory.elements.base;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.stream.Collectors;

public interface Layout extends View {

    @Override
    default String getText() {
        return getWrappedElement().findElements(By.xpath("//*")).stream()
                .filter(el -> StringUtils.isNotEmpty(el.getText()))
                .map(WebElement::getText)
                .collect(Collectors.joining(" "))
                .trim();
    }
}
