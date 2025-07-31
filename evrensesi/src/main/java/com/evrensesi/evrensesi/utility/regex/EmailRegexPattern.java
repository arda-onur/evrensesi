package com.evrensesi.evrensesi.utility.regex;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailRegexPattern {
    public static final String EMAIL_PATTERN = "^" + "([a-zA-Z0-9_\\.\\-+])+"
            + "@" + "[a-zA-Z0-9-.]+"
            + "\\." + "[a-zA-Z0-9-]{2,}"
            + "$";
}
