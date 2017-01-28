package com.clackityclack.lxxcroft;

/**
 * Created by PClegg on 27/01/2017.
 */

public class ParadeDetail {

    public String definition, requirement;

    public ParadeDetail(String d, String r) {
        definition = d;
        requirement = r;
    }

    public void show() {
        System.out.printf("Parade detail with def %s and req %s\n", definition, requirement);
    }

    public String getDef () {
        return definition;
    }

    public String getReq () {
        return requirement;
    }
}

