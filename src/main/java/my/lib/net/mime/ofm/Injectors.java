package my.lib.net.mime.ofm;

import my.lib.net.mime.ofm.injectors.CollectionInjector;
import my.lib.net.mime.ofm.EntityInjector;
import my.lib.net.mime.ofm.injectors.FormDataFieldInjector;

public enum Injectors {

    FIELD(new FormDataFieldInjector()),
    COLLECTION(new CollectionInjector());

    EntityInjector injector;

    Injectors(EntityInjector injector) {
        this.injector = injector;
    }
}
