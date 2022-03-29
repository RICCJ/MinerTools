package MinerTools.ui.utils;

import arc.func.*;
import arc.input.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.ui.*;

public class ElementUtils{
    public static Element addTooltip(Element element, String text, boolean allowMobile){
        return addTooltip(element, text, Align.top, allowMobile);
    }

    public static Element addTooltip(Element element, String text, int align, boolean allowMobile){
        return addTooltip(element, t -> t.background(Styles.black8).margin(4f).add(text).style(Styles.outlineLabel), align, allowMobile);
    }

    public static Element addTooltip(Element element, Cons<Table> cons, boolean allowMobile){
        return addTooltip(element, cons, Align.top, allowMobile);
    }

    /**
     * 为ui元素添加提示
     * @param element 需要添加提示的元素
     * @param cons 自定义的信息编辑
     * @param align 对齐位置
     * @param allowMobile 是否需要手机提示
     * @return 需要添加提示的元素
     */
    public static Element addTooltip(Element element, Cons<Table> cons, int align, boolean allowMobile){
        var tip = new Tooltip(cons){
            @Override
            protected void setContainerPosition(Element element, float x, float y){
                this.targetActor = element;
                Vec2 pos = element.localToStageCoordinates(Tmp.v1.set(0, 0));
                container.pack();
                container.setPosition(pos.x, pos.y, align);
                container.setOrigin(0, element.getHeight());
            }
        };
        tip.allowMobile = allowMobile;
        element.addListener(tip);
        return element;
    }
}
