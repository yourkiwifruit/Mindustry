package mindustry.world.consumers;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import mindustry.world.meta.values.*;

import static mindustry.Vars.*;

public class ConsumeLiquidFilter extends ConsumeLiquidBase{
    public final Boolf<Liquid> filter;

    public ConsumeLiquidFilter(Boolf<Liquid> liquid, float amount){
        super(amount);
        this.filter = liquid;
    }

    @Override
    public void applyLiquidFilter(Bits arr){
        content.liquids().each(filter, item -> arr.set(item.id));
    }

    @Override
    public void build(Building tile, Table table){
        Seq<Liquid> list = content.liquids().select(l -> !l.isHidden() && filter.get(l));
        MultiReqImage image = new MultiReqImage();
        list.each(liquid -> image.add(new ReqImage(liquid.icon(Cicon.medium), () -> tile.liquids != null && tile.liquids.get(liquid) >= use(tile))));

        table.add(image).size(8 * 4);
    }

    @Override
    public String getIcon(){
        return "icon-liquid-consume";
    }

    @Override
    public void update(Building entity){
        entity.liquids.remove(entity.liquids.current(), use(entity));
    }

    @Override
    public boolean valid(Building entity){
        return entity != null && entity.liquids != null && filter.get(entity.liquids.current()) && entity.liquids.currentAmount() >= use(entity);
    }

    @Override
    public void display(BlockStats stats){
        stats.add(booster ? BlockStat.booster : BlockStat.input, new LiquidFilterValue(filter, amount * timePeriod, timePeriod == 60f));
    }
}
