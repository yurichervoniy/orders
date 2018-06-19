package kz.alfabank.alfaordersbpm.domain.models.attachment;

import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;

public enum AttachmentType {
    CLIENT_PHOTO(RetailOrderUiStep.CLIENT_PHOTO),
    IDCARD_FRONTSIDE(RetailOrderUiStep.PASSPORT_PHOTO),
    IDCARD_BACKSIDE(RetailOrderUiStep.PASSPORT_PHOTO),
    AGREEMENT(RetailOrderUiStep.SIGN_FINAL_PRINT),
    SIGNED_AGREEMENT(RetailOrderUiStep.SIGN_FINAL_DOCS),
    SIGNED_PKB_GCVP(RetailOrderUiStep.SIGN);

    private final RetailOrderUiStep step;

    AttachmentType(RetailOrderUiStep step) {
        this.step = step;
    }

    public RetailOrderUiStep getStep() {
        return step;
    }

    // OtherPhoto,  PkbAndGcvp, SignedPkbAndGcvp, Dogovor
}
