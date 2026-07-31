import { useEffect, useState } from "react";
import { getActivityDetail } from "../services/api";
import { useParams } from "react-router-dom";
import { Box, Card, CardContent, Divider, Typography } from "@mui/material";

const ActivityDetail = () => {
    const { id } = useParams();
    const [activity, setActivity] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchActivityDetail = async () => {
            try {
                setLoading(true);
                const response = await getActivityDetail(id);
                setActivity(response.data);
                setError(null);
            } catch (error) {
                console.log(error);
                setError("Failed to fetch activity details");
            } finally {
                setLoading(false);
            }
        };
        fetchActivityDetail();
    }, [id]);

    // Loading state - rendered in the main return, not inside useEffect
    if (loading) {
        return (
            <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
                <Typography>Loading...</Typography>
            </Box>
        );
    }

    // Error state
    if (error) {
        return (
            <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
                <Typography color="error">{error}</Typography>
            </Box>
        );
    }

    // No activity found
    if (!activity) {
        return (
            <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
                <Typography>Activity not found</Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
            <Card sx={{ mb: 2 }}>
                <CardContent>
                    <Typography variant="h5" gutterBottom>Activity Details</Typography>
                    <Typography>Type: {activity.type}</Typography>
                    <Typography>Duration: {activity.duration}</Typography>
                    <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
                    <Typography>Date: {new Date(activity.createdAt).toLocaleString()}</Typography>
                </CardContent>
            </Card>

            {activity.recommendation && (
                <Card>
                    <CardContent>
                        <Typography variant="h5" gutterBottom>AI Recommendation</Typography>
                        <Typography variant="h6">Analysis</Typography>
                        <Typography paragraph>{activity.recommendation}</Typography>
                        <Divider sx={{ my: 2 }} />

                        {activity.improvements && activity.improvements.length > 0 && (
                            <>
                                <Typography variant="h6">Improvements</Typography>
                                {activity.improvements.map((improvement, index) => (
                                    <Typography key={index} paragraph>{improvement}</Typography>
                                ))}
                                <Divider sx={{ my: 2 }} />
                            </>
                        )}

                        {activity.suggestions && activity.suggestions.length > 0 && (
                            <>
                                <Typography variant="h6">Suggestions</Typography>
                                {activity.suggestions.map((suggestion, index) => (
                                    <Typography key={index} paragraph>{suggestion}</Typography>
                                ))}
                                <Divider sx={{ my: 2 }} />
                            </>
                        )}

                        {activity.safety && activity.safety.length > 0 && (
                            <>
                                <Typography variant="h6">Safety Guidelines</Typography>
                                {activity.safety.map((safety, index) => (
                                    <Typography key={index} paragraph>{safety}</Typography>
                                ))}
                            </>
                        )}
                    </CardContent>
                </Card>
            )}
        </Box>
    );
};

export default ActivityDetail;