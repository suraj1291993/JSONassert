package org.skyscreamer.jsonassert.comparator;

import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.util.Arrays;
import java.util.Collection;

public class CustomComparator extends DefaultComparator {

    private final Collection<Customization> customizations;

    public CustomComparator(JSONCompareMode mode,  Customization... customizations) {
        super(mode);
        this.customizations = Arrays.asList((Customization[])customizations);
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
    	Customization customization = getCustomization(prefix);
        if (customization != null) {
        	try {
    			if (!customization.matches(prefix, actualValue, expectedValue, result)) {
   					result.fail(prefix, expectedValue, actualValue);
    			}
        	}
        	catch (ValueMatcherException e) {
       			result.fail(prefix, e);
        	}
        } else {
            super.compareValues(prefix, expectedValue, actualValue, result);
        }
    }

    private Customization getCustomization(String path) {
        for (Customization c : customizations)
            if (c.appliesToPath(path))
                return c;
        return null;
    }
}
