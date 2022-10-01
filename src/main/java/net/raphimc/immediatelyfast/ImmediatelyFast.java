package net.raphimc.immediatelyfast;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImmediatelyFast implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("ImmediatelyFast");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Loaded ImmediatelyFast");
    }

}
