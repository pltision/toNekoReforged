package yee.pltision.tonekoreforged.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PointedEndRodPiece implements StringRepresentable {
   BASE("base",false),
   BASE_TIP("base_tip",true),
   TIP("tip",true),
   MIDDLE("middle",false);

   final String name;
   final boolean isTip;

   PointedEndRodPiece(String p_156018_, boolean isTip) {
      this.name = p_156018_;
       this.isTip = isTip;
   }

   public String toString() {
      return this.name;
   }

   boolean isTip(){
      return isTip;
   }

   public @NotNull String getSerializedName() {
      return this.name;
   }
}