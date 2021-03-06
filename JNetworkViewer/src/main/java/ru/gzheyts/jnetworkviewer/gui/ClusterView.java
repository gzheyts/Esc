package ru.gzheyts.jnetworkviewer.gui;

import com.javadocking.dock.Dock;
import com.javadocking.dock.SingleDock;
import com.javadocking.dockable.*;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.drag.DragListener;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import net.inference.database.dto.Cluster;
import ru.gzheyts.jnetworkviewer.loader.DatabaseLoader;
import ru.gzheyts.jnetworkviewer.model.Network;
import ru.gzheyts.jnetworkviewer.model.convertеrs.ToStringConverter;

import java.awt.*;

/**
 * @author gzheyts
 */
public class ClusterView extends mxGraphComponent implements DraggableContent {

    private Cluster cluster;
    private SingleDock dock;

    public ClusterView(Network network, Cluster cluster) {
        super(network);
        this.cluster = cluster;
        setEnabled(false);
        setPreferredSize(new Dimension(200, 400));
        //todo : load in background
        initNetwork();

    }

    public void addDragListener(DragListener dragListener) {
        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
    }


    public String getTitle() {
        return ToStringConverter.convert(cluster);
    }


    public Dock getDock() {
        if (dock == null) {
            createDock();
        }

        return dock;
    }

    public void initNetwork() {
        DatabaseLoader.loadCluster((Network) getGraph(), cluster);
        new mxCircleLayout(getGraph(), 100).execute(getGraph().getDefaultParent());

    }

    private void createDock() {
        dock = new SingleDock();

        DefaultDockable dockable = new DefaultDockable(getClass().getSimpleName(), this, getTitle(), null,
                DockingMode.SINGLE + DockingMode.BOTTOM);

        Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), new int[0]);
        int[] states = {DockableState.NORMAL, DockableState.CLOSED};
        wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), states);

        dock.addDockable(wrapper, SingleDock.SINGLE_POSITION);
    }
}
