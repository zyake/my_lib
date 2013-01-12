package my.lib.net.mime.ofm;

import my.lib.net.mime.ofm.converters.DoubleEntityConverter;
import my.lib.net.mime.ofm.EntityConverter;
import my.lib.net.mime.ofm.converters.IntegerEntityConvereter;
import my.lib.net.mime.ofm.converters.TextEntityConverter;

public enum Converters {

    Double(new DoubleEntityConverter()),
    Text(new TextEntityConverter()),
    Integer(new IntegerEntityConvereter());

    EntityConverter converter;

    Converters(EntityConverter converter) {
        this.converter = converter;
    }
}
