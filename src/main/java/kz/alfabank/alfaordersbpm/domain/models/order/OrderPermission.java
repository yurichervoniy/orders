package kz.alfabank.alfaordersbpm.domain.models.order;

import org.hibernate.annotations.RowId;

import javax.persistence.*;

@Entity
@Table(name = "order_permission")
@RowId("ROWID")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "addOrderPermission",
                procedureName = "retail_orders.addOrderPermission",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_order_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_permission", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_grant_to", type = String.class)
                })
})
public class OrderPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_perm_gen")
    @SequenceGenerator(name = "orders_perm_gen", sequenceName = "order_permission_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "targettype", length = 500, nullable = false)
    private String targetType;

    @Column(name = "targetid", nullable = false)
    private Long targetId;

    @Column(name = "permission", length = 500, nullable = false)
    private String permission;

    @Column(name = "grant_to", length = 500, nullable = false)
    private String grantTo;

    @Column(name = "grant_orgcode", length = 100)
    private String grantOrgcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getGrantTo() {
        return grantTo;
    }

    public void setGrantTo(String grantTo) {
        this.grantTo = grantTo;
    }

    public String getGrantOrgcode() {
        return grantOrgcode;
    }

    public void setGrantOrgcode(String grantOrgcode) {
        this.grantOrgcode = grantOrgcode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderPermission{");
        sb.append("id=").append(id);
        sb.append(", targetType='").append(targetType).append('\'');
        sb.append(", targetId=").append(targetId);
        sb.append(", permission='").append(permission).append('\'');
        sb.append(", grantTo='").append(grantTo).append('\'');
        sb.append(", grantOrgcode='").append(grantOrgcode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
