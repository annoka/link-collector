package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotDirDeleted extends SnapshotsUpdate {
	
	public final SnapshotDir dir;

	public SnapshotDirDeleted(SnapshotDir dir) {
		super();
		this.dir = dir;
	}
	
	
}
