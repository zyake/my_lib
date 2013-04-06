package my.lib.data.trn;

import java.util.Stack;

public class TrnResourceRegistry {

    private static ThreadLocal<Stack<TrnResource>> ongoingResources = new ThreadLocal<Stack<TrnResource>>() {
        @Override
        protected Stack<TrnResource> initialValue() {
            return new Stack<TrnResource>();
        }
    };

    public static TrnResource peekCurrentTrnResource() {
        boolean noResourceFound = ongoingResources.get().isEmpty();
        if ( noResourceFound ) {
            throw new TrnDataAccessException("トランザクションリソースが登録されていません。");
        }

        return ongoingResources.get().peek();
    }

    public static TrnResource popCurrentTrnResource() {
        boolean noResourceFound = ongoingResources.get().isEmpty();
        if ( noResourceFound ) {
            throw new TrnDataAccessException("トランザクションリソースが登録されていません。");
        }

        return ongoingResources.get().pop();
    }

    public static void pushTrnResource(TrnResource trnResource) {
        ongoingResources.get().push(trnResource);
    }
}
