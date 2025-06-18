import React, { useState } from 'react';
import {
  Container,
  TextField,
  Button,
  Card,
  CardContent,
  Typography,
  Pagination,
  Box,
  CircularProgress,
} from '@mui/material';
import axios from 'axios';

function App() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalResults, setTotalResults] = useState(0);
  const itemsPerPage = 10;

  const handleSearch = async () => {
    if (!query.trim()) return;
    
    setLoading(true);
    try {
      const response = await axios.get(`/search?query=${encodeURIComponent(query)}`);
      setResults(response.data);
      setTotalResults(response.data.length);
      setPage(1);
    } catch (error) {
      console.error('Error fetching results:', error);
    } finally {
      setLoading(false);
    }
  };

  const highlightText = (text, searchQuery) => {
    if (!searchQuery) return text;
    
    // 简单的分词实现：按空格和标点符号分割
    const words = searchQuery.split(/[\s,，.。!！?？;；:：]/).filter(word => word.length > 0);
    let highlightedText = text;
    
    words.forEach(word => {
      if (word.length > 1) {
        const regex = new RegExp(word, 'gi');
        highlightedText = highlightedText.replace(regex, match => `<span style="color: red">${match}</span>`);
      }
    });
    
    return highlightedText;
  };

  const getCurrentPageResults = () => {
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    return results.slice(startIndex, endIndex);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4, display: 'flex', gap: 2, justifyContent: 'center' }}>
        <TextField
          sx={{ width: '400px' }}
          label="搜索"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <Button
          variant="contained"
          onClick={handleSearch}
          disabled={loading}
        >
          搜索
        </Button>
      </Box>

      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          <Typography variant="h6" sx={{ mb: 2 }}>
            找到 {totalResults} 个结果
          </Typography>

          {getCurrentPageResults().map((result) => (
            <Card key={result.id} sx={{ mb: 2 }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {result.fullName}
                </Typography>
                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{ mb: 1 }}
                >
                  <a href={result.htmlUrl} target="_blank" rel="noopener noreferrer">
                    {result.htmlUrl}
                  </a>
                </Typography>
                <Typography
                  variant="body2"
                  dangerouslySetInnerHTML={{
                    __html: highlightText(result.readme, query)
                  }}
                />
              </CardContent>
            </Card>
          ))}

          {totalResults > 0 && (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
              <Pagination
                count={Math.ceil(totalResults / itemsPerPage)}
                page={page}
                onChange={(e, value) => setPage(value)}
                color="primary"
              />
            </Box>
          )}
        </>
      )}
    </Container>
  );
}

export default App; 