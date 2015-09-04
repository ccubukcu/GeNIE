package com.genie.enums;

/**
 * @author ccubukcu
 *
 */
public enum ResourceBundles {
	
	LABELS("localization.labels"),
    MESSAGES("localization.messages"),
    MAILS("localization.mails");
    
    private String bundleName;

    private ResourceBundles(String _bundleName) {

            this.bundleName = _bundleName;

    }

    public String getBundleName() {

            return bundleName;

    }
}
