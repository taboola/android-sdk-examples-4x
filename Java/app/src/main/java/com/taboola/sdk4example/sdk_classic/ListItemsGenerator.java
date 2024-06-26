package com.taboola.sdk4example.sdk_classic;

import android.graphics.Color;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ListItemsGenerator {

    public abstract static class FeedListItem {

        @Retention(SOURCE)
        @IntDef({ItemType.TABOOLA_ITEM, ItemType.TABOOLA_MID_ITEM, ItemType.RANDOM_ITEM,})

        public @interface ItemType {
            int TABOOLA_ITEM = 1;
            int TABOOLA_MID_ITEM = 3;
            int RANDOM_ITEM = 2;
        }

        @ItemType
        public int type;

        FeedListItem(@ItemType int type) {
            this.type = type;
        }
    }

    public static class TaboolaView extends FeedListItem {

        TaboolaView() {
            super(ItemType.TABOOLA_ITEM);
        }
    }

    public static class TaboolaMidView extends FeedListItem {

        TaboolaMidView() {
            super(ItemType.TABOOLA_MID_ITEM);
        }
    }

    public static class RandomItem extends FeedListItem {

        public final int color;
        public final String randomText;

        RandomItem(int color) {
            super(ItemType.RANDOM_ITEM);
            this.color = color;
            randomText = getRandomText();
        }

        private String getRandomText() {
            Random random = new Random();
            int newValue = random.nextInt();

            switch (newValue % 10) {

                default:
                case 1:
                case 9:
                    return "Jack Tar me keel lanyard grog doubloon gabion furl Yellow Jack flogging. Booty galleon cable Sail ho lugsail yard draught driver run a rig draft. Wench nipperkin mutiny bowsprit lad Arr draft overhaul salmagundi bilged on her anchor.";

                case 2:
                case 8:
                    return "SFire in the hole grapple plunder matey swing the lead clipper hang the jib skysail handsomely Blimey. Sink me poop deck pressgang hands Yellow Jack lugger haul wind run a rig Sea Legs walk the plank. Boatswain fluke pinnace salmagundi nipper plunder landlubber or just lubber gibbet barque topgallant";

                case 3:
                case 7:
                    return "Transom bilged on her anchor deadlights Chain Shot topgallant doubloon lugger code of conduct man-of-war spanker. Cackle fruit ahoy fore walk the plank main sheet crimp ye nipper coffer Sea Legs. Bowsprit gangplank long boat gangway strike colors parley gibbet jib take a caulk fathom.";

                case 4:
                case 6:
                    return "Shrimps paste has to have a sun-dried, puréed carrots component. Herring tastes best with fish sauce and lots of cinnamon. Flatten a dozen oysters, tuna, and cumin in a large bucket over medium heat, warm for a handfull minutes and whisk with some squid.";

                case 5:
                    return "Stern capstan chantey sloop Sea Legs ye Jack Tar rigging sheet booty. Jack Tar jib hogshead grog belay cable keelhaul sloop Shiver me timbers jolly boat. Grog blossom lanyard handsomely chandler run a rig dance the hempen jig Spanish Main landlubber or just lubber measured fer yer chains topsail.";

            }
        }
    }

    public static List<FeedListItem> getGeneratedData(boolean withMidView) {
        List<FeedListItem> randomImages = new ArrayList<>();
        randomImages.add(new RandomItem(Color.GRAY));
        randomImages.add(new RandomItem(Color.RED));
        randomImages.add(new RandomItem(Color.BLACK));
        randomImages.add(new RandomItem(Color.MAGENTA));
        randomImages.add(new RandomItem(Color.BLUE));
        randomImages.add(new RandomItem(Color.YELLOW));
        randomImages.add(new RandomItem(Color.GREEN));
        randomImages.add(new RandomItem(Color.DKGRAY));
        Collections.shuffle(randomImages);


        if (withMidView) {
            randomImages.add(2, new TaboolaMidView());
        }

        randomImages.add(new TaboolaView());
        return randomImages;
    }


    public static List<FeedListItem> getGeneratedDataForWidgetDynamic() {
        return getGeneratedDataForWidgetDynamic(false);
    }

    public static List<FeedListItem> getGeneratedDataForWidgetDynamic(boolean withBottomView) {
        List<FeedListItem> randomImages = new ArrayList<>();
        randomImages.add(new RandomItem(Color.parseColor("#c9eae1")));
        randomImages.add(new RandomItem(Color.parseColor("#acdadc")));
        randomImages.add(new TaboolaMidView());
        randomImages.add(new RandomItem(Color.parseColor("#fada5e")));
        randomImages.add(new RandomItem(Color.parseColor("#d0e4b2")));
        randomImages.add(new RandomItem(Color.parseColor("#d7858e")));
        if (withBottomView) {
            randomImages.add(new TaboolaView());
        }
        return randomImages;
    }
}
