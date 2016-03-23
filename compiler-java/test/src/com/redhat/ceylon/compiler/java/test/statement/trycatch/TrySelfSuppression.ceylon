class TrySelfSuppression()
        satisfies Destroyable {
    shared actual void destroy(Throwable? error) {
        if (exists error) {
            throw error;
        }
    }
}
class TrySelfSuppressionException() 
        extends Exception() {
}

void trySelfSuppression() {
    try {
        try(validatorProgress = TrySelfSuppression()) {
            if (1==1) {
                throw TrySelfSuppressionException();
            }
        }
        throw;
    } catch (TrySelfSuppressionException e) {
        // that's OK, anything else is not
    }
}
