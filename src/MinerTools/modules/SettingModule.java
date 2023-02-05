package MinerTools.modules;

import MinerTools.*;
import MinerTools.ui.*;
import MinerTools.ui.settings.*;
import MinerTools.ui.settings.CategorySetting.*;
import arc.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

public abstract class SettingModule extends AbstractModule<SettingModule>{
    public final String enableSettingName;

    public SettingModule(String name){
        this(null, name);
    }

    public SettingModule(SettingModule parent, String name){
        super(parent, name);
        
        enableSettingName = name + "Enable";
        enable = MinerVars.settings.getBool(enableSettingName, true);
    }

    @Override
    public void load(){
        if(parent == null){
            MinerVars.ui.settings.modules.addCategory(name, this::setSettings, getBuilder(this));
        }

        for(SettingModule child : children){
            child.load();
        }
    }

    public void setSettings(MSettingTable settings){
        for(SettingModule child : children){
            settings.addCategory(child.name, child::setSettings, getBuilder(child));
        }
    }
    
    @Override
    public void setEnable(boolean enable){
        super.setEnable(enable);
        MinerVars.settings.put(enableSettingName, enable, false, true);
    }
        
    @Override
    public void toggle(){
        boolean enable = MinerVars.settings.getBool(enableSettingName, true);
        setEnable(!enable);
    }

    private static CategoryBuilder getBuilder(SettingModule module){
        return (container, category) -> {
            container.table(table -> {
                table.button(button -> {
                    button.add(category.localizedName()).left();
                    button.label(() -> {
                        return module.isEnable() ? Core.bundle.get("enabled") : Core.bundle.get("disabled");
                    }).growX().right().style(Styles.outlineLabel);
                }, MStyles.clearToggleTranst, module::toggle)
                .minWidth(150f).height(32f).checked(b -> module.isEnable()).growX();
                
                table.button(b -> {
                    b.image(() -> {
                        return (category.isShown() ? Icon.downSmall : Icon.upSmall).getRegion();
                    }).size(24f);
                }, Styles.clearNoneTogglei, category::toggle).size(32f)
                .checked(b -> category.isShown());
            }).growX();

            container.row();

            container.table(Tex.pane2, t -> {
                t.collapser(category, true, () -> module.isEnable() && category.isShown())
                .growX().get().setDuration(0.4f);
            }).growX().padLeft(12f);
        };
    }
}
