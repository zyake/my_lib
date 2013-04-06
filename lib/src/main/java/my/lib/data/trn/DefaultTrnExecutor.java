package my.lib.data.trn;

import my.lib.util.ExceptionHandler;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultTrnExecutor implements TrnExecutor {

    private static final  Logger LOGGER = Logger.getLogger(DefaultTrnExecutor.class.getName());

    private static final ExceptionHandler NULL_EXCEPTION_HANDLER = new ExceptionHandler() {
        @Override
        public void initialize(Map<String, Object> config) {
        }
        @Override
        public void handleException(Exception ex) {
        }
    };

    private ExceptionHandler exceptionHandler;

    private TrnResourceFactory resourceFactory;

    @Override
    public void initialize(Map<String, Object> config) {
        exceptionHandler = (ExceptionHandler) config.get(CONFIG_EXCEPTION_HANDLER);
        boolean exceptionHandlerRequired = exceptionHandler == null;
        if ( exceptionHandlerRequired ) {
            this.exceptionHandler = NULL_EXCEPTION_HANDLER;
        }

        resourceFactory = (TrnResourceFactory) config.get(CONFIG_RESOURCE_FACTORY);
        boolean resourceFactoryRequired = resourceFactory == null;
        if ( resourceFactoryRequired ) {
            throw new TrnDataAccessException("リソースファクトリが指定されていません。");
        }
    }

    @Override
    public void execute(TrnAction trnAction) {
        if ( LOGGER.isLoggable(Level.FINE) ) {
            LOGGER.fine("start transaction...: trn id=" + trnAction);
        }

        TrnResource resource = resourceFactory.createResource();
        TrnResourceRegistry.pushTrnResource(resource);
        try {
            trnAction.run();
            resource.commit();

            if ( LOGGER.isLoggable(Level.FINE) ) {
                LOGGER.fine("transaction succeeded: trn id=" + trnAction);
            }
        } catch (Exception ex) {
            exceptionHandler.handleException(ex);
            resource.rollback();
            if ( LOGGER.isLoggable(Level.FINE) ) {
                LOGGER.fine("transaction failed: trn id=" + trnAction);
            }
        } finally {
            TrnResourceRegistry.popCurrentTrnResource();
            resource.close();
        }
    }
}
