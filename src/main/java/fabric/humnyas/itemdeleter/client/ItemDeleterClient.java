package fabric.humnyas.itemdeleter.client;

import fabric.humnyas.itemdeleter.compat.emi.EMIIntegration;
import fabric.humnyas.itemdeleter.compat.rei.REIIntegration;
import net.fabricmc.api.ClientModInitializer;

import static fabric.humnyas.itemdeleter.compat.ModPresenceUtil.*;

public class ItemDeleterClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        if (isEMIPresent()) EMIIntegration.initialise();

        if (isREIPresent()) REIIntegration.initialise();
    }
}
