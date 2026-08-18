/*
 * Author: Tan Kwan Hong
 * Date: 28 May 2026
 * Description: Groups sorted records together with sorting metrics.
 */

public class SortResult
{
    private PickupRecord[] records;
    private SortMetrics metrics;

    public SortResult(PickupRecord[] records, SortMetrics metrics)
    {
        this.records = records;
        this.metrics = metrics;
    }

    public PickupRecord[] getRecords()
    {
        return records;
    }

    public void setRecords(PickupRecord[] records)
    {
        this.records = records;
    }

    public SortMetrics getMetrics()
    {
        return metrics;
    }

    public void setMetrics(SortMetrics metrics)
    {
        this.metrics = metrics;
    }
}
