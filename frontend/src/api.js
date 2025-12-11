import axios from 'axios';

export const api = axios.create({
  baseURL: '',
  headers: { 'Content-Type': 'application/json' }
});

export async function listMatches() {
  const { data } = await api.get('/matches');
  return data;
}

export async function listFinishedMatches() {
  const { data } = await api.get('/matches/finished');
  return data;
}

export async function createPlayer(name, initialBalance) {
  const { data } = await api.post('/players', { name, initialBalance: Number(initialBalance) });
  return data;
}

export async function fetchPlayer(name) {
  const { data } = await api.get(`/players/${encodeURIComponent(name)}`);
  return data;
}

export async function placeSingleBet(payload) {
  const { data } = await api.post('/bets/single', payload);
  return data;
}

export async function placeCombinationBet(payload) {
  const { data } = await api.post('/bets/combination', payload);
  return data;
}

export async function listBets(playerName, statuses) {
  const params = statuses && statuses.length ? { status: statuses } : {};
  const { data } = await api.get(`/players/${encodeURIComponent(playerName)}/bets`, { params });
  return data;
}

export async function listTransactions(playerName) {
  const { data } = await api.get(`/players/${encodeURIComponent(playerName)}/transactions`);
  return data;
}

