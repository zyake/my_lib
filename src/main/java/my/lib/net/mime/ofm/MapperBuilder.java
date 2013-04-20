package my.lib.net.mime.ofm;

import my.lib.net.mime.ofm.acceptors.FormDataNameAcceptor;
import my.lib.net.mime.ofm.converters.DateEntityConverter;

import java.util.ArrayList;
import java.util.List;

public class MapperBuilder {

    private List<ConverterHolder> holders = new ArrayList<ConverterHolder>();

    private EntityInjector injector;

    public MapperBuilder addHolder(EntityConverter converter, EntityAcceptor acceptor) {
        return doAddHolder(converter, acceptor);
    }


    public MapperBuilder addHolder(Converters converter, EntityAcceptor acceptor) {
        return doAddHolder(converter.converter, acceptor);
    }

    public MapperBuilder addDateHolder(String field, String format) {
        return doAddHolder(new DateEntityConverter(format), new FormDataNameAcceptor(field));
    }

    public MapperBuilder addHolder(Converters converter, String field) {
        return doAddHolder(converter.converter, new FormDataNameAcceptor(field));
    }

    public MapperBuilder setInjector(Injectors injectors) {
        this.injector = injectors.injector;

        return this;
    }

    public MultipartMessageMapper build() {
        boolean requireAtLeastOneHolder = holders.isEmpty();
        if ( requireAtLeastOneHolder ) {
            throw new MIMEConvertException("require at least one holder");
        }

        boolean requireInjector = injector == null;
        if ( requireInjector ) {
            throw new MIMEConvertException("require setInjector");
        }

        MultipartMessageMapper mapper = new DefaultMultipartMessageMapper(holders, injector);

        return mapper;
    }

    private MapperBuilder doAddHolder(EntityConverter converter, EntityAcceptor acceptor) {
        ConverterHolder holder = new ConverterHolder(acceptor, converter);
        holders.add(holder);

        return this;
    }
}
