package org.csn;

import org.csn.component.Coordinator;
import org.csn.component.impl.CoordinatorImpl;
import org.csn.data.CSNConfiguration;

public class CSNProvider {
    private static Coordinator coordinator = null;

    public static Coordinator getCSNCoreService(String CSN_Name) {
        if(coordinator == null) {
            coordinator = new CoordinatorImpl(CSN_Name);
            return coordinator;
        }

        return coordinator;
    }

    public static Coordinator getCSNCoreService(String CSN_Name, CSNConfiguration config) {
        if(coordinator == null) {
            coordinator = new CoordinatorImpl(CSN_Name, config);
            return coordinator;
        }

        return coordinator;
    }
}
