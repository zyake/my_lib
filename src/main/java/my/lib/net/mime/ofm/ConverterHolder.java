package my.lib.net.mime.ofm;

public class ConverterHolder {

    private EntityAcceptor acceptor;

    private EntityConverter converter;

    public ConverterHolder(EntityAcceptor acceptor, EntityConverter converter) {
        this.acceptor = acceptor;
        this.converter = converter;
    }

    public EntityAcceptor getAcceptor() {
        return acceptor;
    }

    public EntityConverter getConverter() {
        return converter;
    }
}
