package drgMB;

public class SegmentTile extends HexTile {
	public enum TileType {
		GROUND,
		PIT,
		FLARE
	}
	
	private MapTile anchorTile;
	private TileType tileType;
	private MiniFigure mini;

	public SegmentTile(MapTile anchorTile, TileType tileType) {
		this.anchorTile = anchorTile;
		this.tileType = tileType;
	}
	
	public SegmentTile getAdjacentTile(int position) {
		return (SegmentTile)super.getAdjacentTile(position);
	}
	
	public MapTile getAnchorTile() {
		return this.anchorTile;
	}
	
	public void setAnchorTile(MapTile anchorTile) {
		this.anchorTile = anchorTile;
	}

	public TileType getTileType() {
		return this.tileType;
	}

	public MiniFigure getMini() {
		return mini;
	}

	public void setMini(MiniFigure mini) {
		this.mini = mini;
	}
}
