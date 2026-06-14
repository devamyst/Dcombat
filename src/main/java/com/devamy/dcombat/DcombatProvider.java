package com.devamy.dcombat;

public final class DcombatProvider {

    private static DcombatApi DCOMBAT_API;

    public static DcombatApi provide() {
        if (DCOMBAT_API == null) {
            throw new IllegalStateException("DcombatApi has not been initialized yet!");
        }

        return DCOMBAT_API;
    }

    static void initialize(DcombatApi dcombatApi) {
        if (DCOMBAT_API != null) {
            throw new IllegalStateException("DcombatApi has already been initialized!");
        }

        DCOMBAT_API = dcombatApi;
    }

    static void deinitialize() {
        if (DCOMBAT_API == null) {
            throw new IllegalStateException("DcombatApi has not been initialized yet!");
        }

        DCOMBAT_API = null;
    }

}
