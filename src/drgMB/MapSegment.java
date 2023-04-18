package drgMB;

import java.util.HashSet;
import java.util.UUID;

public class MapSegment {
	private UUID mapSegmentUUID;
	private SegmentTile originTile;
	private HashSet<SegmentTile> segmentTiles;
	
	public MapSegment(SegmentTile origin, HashSet<SegmentTile> segmentTiles) {
		this.mapSegmentUUID = UUID.randomUUID();
		this.originTile = origin;
		this.segmentTiles = segmentTiles;
	}
	
	public SegmentTile getOrigin() {
		return this.originTile;
	}
	
	public HashSet<SegmentTile> getSegmentTiles() {
		HashSet<SegmentTile> returner = new HashSet<SegmentTile>();
		returner.addAll(this.segmentTiles);
		return returner;
	}
	
	public UUID getUUID() {
		return this.mapSegmentUUID;
	}
}
