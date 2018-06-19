package kz.alfabank.alfaordersbpm.domain.models.order;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.CommonServiceRef;
import org.hibernate.annotations.RowId;

import javax.persistence.*;


@Entity
@DiscriminatorValue("RETAIL")
@RowId("ROWID")
public class RetailOrder extends AbstractOrder {

    @ApiModelProperty(notes="Привлеченец")
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "value", column = @Column(name = "attracted_code")),
            @AttributeOverride(name = "text", column = @Column(name = "attracted_name")),
            @AttributeOverride(name = "dictName", column = @Column(name = "attracted_source")),
            @AttributeOverride(name = "dictLang", column = @Column(name = "attracted_lang"))
    })
    private CommonServiceRef attractedRef;

    public CommonServiceRef getAttractedRef() {
        return attractedRef;
    }

    public void setAttractedRef(CommonServiceRef attractedRef) {
        this.attractedRef = attractedRef;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RetailOrder{");
        sb.append("attractedRef=").append(attractedRef);
        sb.append('}');
        sb.append(super.toString());
        return sb.toString();
    }
}
