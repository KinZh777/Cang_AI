package cn.zx.cang.ai.api.collection.model;

import cn.zx.cang.ai.api.collection.constant.CollectionStateEnum;
import cn.zx.cang.ai.api.collection.constant.CollectionVoState;
import cn.zx.cang.ai.api.goods.model.BaseGoodsVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author kinchou
 */
@Getter
@Setter
@ToString
public class CollectionVO extends BaseGoodsVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * '藏品名称'
     */
    private String name;

    /**
     * '藏品封面'
     */
    private String cover;

    /**
     * '藏品类目id'
     */
    private String classId;

    /**
     * '价格'
     */
    private BigDecimal price;

    /**
     * '藏品数量'
     */
    private Long quantity;

    /**
     * '库存'
     */
    private Long inventory;

    /**
     * '状态'
     */
    private CollectionVoState state;

    /**
     * '藏品创建时间'
     */
    private Date createTime;

    /**
     * '藏品发售时间'
     */
    private Date saleTime;

    /**
     * '藏品上链时间'
     */
    private Date syncChainTime;

    /**
     * 版本
     */
    private Integer version;

    public static final int DEFAULT_MIN_SALE_TIME = 60;

    public void setState(CollectionStateEnum state, Date saleTime, Long saleableInventory) {
        if (state.equals(CollectionStateEnum.INIT) || state.equals(CollectionStateEnum.REMOVED)) {
            this.setState(CollectionVoState.NOT_FOR_SALE);
        }

        Instant now = Instant.now();

        if (now.compareTo(saleTime.toInstant()) >= 0) {
            if (saleableInventory > 0) {
                this.setState(CollectionVoState.SELLING);
            } else {
                this.setState(CollectionVoState.SOLD_OUT);
            }
        } else {
            if (ChronoUnit.MINUTES.between(now, saleTime.toInstant()) > DEFAULT_MIN_SALE_TIME) {
                this.setState(CollectionVoState.WAIT_FOR_SALE);
            } else {
                this.setState(CollectionVoState.COMING_SOON);
            }
        }
    }

    @Override
    public String getGoodsName() {
        return this.name;
    }

    @Override
    public String getGoodsPicUrl() {
        return this.cover;
    }

    @Override
    public String getSellerId() {
        //藏品持有人默认是平台,平台ID用O表示
        return "0";
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Boolean available() {
        return this.state == CollectionVoState.SELLING;
    }

    @Override
    public BigDecimal getPrice() {
        return this.price;
    }
}
