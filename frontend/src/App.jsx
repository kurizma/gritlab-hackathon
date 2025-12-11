import { useEffect, useMemo, useState } from 'react';
import {
  listMatches,
  listFinishedMatches,
  createPlayer,
  fetchPlayer,
  placeSingleBet,
  placeCombinationBet,
  listBets,
  listTransactions
} from './api';

const outcomes = ['HOME', 'DRAW', 'AWAY'];
const statusOptions = ['PLACED', 'WON', 'LOST'];

export default function App() {
  const [matches, setMatches] = useState([]);
  const [finishedMatches, setFinishedMatches] = useState([]);
  const [playerName, setPlayerName] = useState('');
  const [initialBalance, setInitialBalance] = useState(100);
  const [currentPlayer, setCurrentPlayer] = useState(null);
  const [single, setSingle] = useState({ matchId: '', outcome: 'HOME', stake: 10 });
  const [comboSelections, setComboSelections] = useState([]);
  const [comboStake, setComboStake] = useState(10);
  const [bets, setBets] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [statusFilter, setStatusFilter] = useState([]);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    refreshMatches();
    refreshFinishedMatches();
  }, []);

  useEffect(() => {
    if (currentPlayer) {
      refreshHistory(currentPlayer.name);
    }
  }, [currentPlayer, statusFilter]);

  const refreshMatches = async () => {
    try {
      const data = await listMatches();
      setMatches(data);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const refreshFinishedMatches = async () => {
    try {
      const data = await listFinishedMatches();
      setFinishedMatches(data);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const loadPlayer = async () => {
    setError('');
    setMessage('');
    try {
      const data = await fetchPlayer(playerName);
      setCurrentPlayer(data);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const addPlayer = async () => {
    setError('');
    setMessage('');
    try {
      const data = await createPlayer(playerName, initialBalance);
      setCurrentPlayer(data);
      setMessage('Player created');
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const submitSingle = async () => {
    if (!currentPlayer) return setError('Select a player first');
    setError('');
    setMessage('');
    try {
      await placeSingleBet({
        playerName: currentPlayer.name,
        matchId: single.matchId,
        outcome: single.outcome,
        stake: Number(single.stake)
      });
      setMessage('Single bet placed');
      await refreshHistory(currentPlayer.name);
      await refreshMatches();
      const updated = await fetchPlayer(currentPlayer.name);
      setCurrentPlayer(updated);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const submitCombination = async () => {
    if (!currentPlayer) return setError('Select a player first');
    if (comboSelections.length < 2) return setError('Add at least two selections');
    setError('');
    setMessage('');
    try {
      await placeCombinationBet({
        playerName: currentPlayer.name,
        selections: comboSelections.map((s) => ({
          matchId: s.matchId,
          outcome: s.outcome
        })),
        stake: Number(comboStake)
      });
      setMessage('Combination bet placed');
      await refreshHistory(currentPlayer.name);
      await refreshMatches();
      const updated = await fetchPlayer(currentPlayer.name);
      setCurrentPlayer(updated);
      setComboSelections([]);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const refreshHistory = async (name) => {
    try {
      const betsData = await listBets(name, statusFilter);
      setBets(betsData);
      const txData = await listTransactions(name);
      setTransactions(txData);
    } catch (e) {
      setError(e.response?.data?.message || e.message);
    }
  };

  const addSelection = (matchId) => {
    const match = matches.find((m) => m.id === matchId);
    if (!match) return;
    setComboSelections((prev) => [...prev, { matchId, outcome: 'HOME', label: `${match.homeTeam} vs ${match.awayTeam}` }]);
  };

  const removeSelection = (idx) => {
    setComboSelections((prev) => prev.filter((_, i) => i !== idx));
  };

  const sortedMatches = useMemo(() => {
    return [...matches].sort((a, b) => new Date(a.kickoffAt) - new Date(b.kickoffAt));
  }, [matches]);

  const combinedOdds = useMemo(() => {
    return comboSelections.reduce((acc, sel) => {
      const match = matches.find((m) => m.id === sel.matchId);
      if (!match) return acc;
      const odds = sel.outcome === 'HOME' ? match.homeOdds : sel.outcome === 'DRAW' ? match.drawOdds : match.awayOdds;
      return acc * odds;
    }, 1);
  }, [comboSelections, matches]);

  const matchById = (id) => matches.find((m) => m.id === id);

  const singleSelectedOdds = useMemo(() => {
    const m = matchById(single.matchId);
    if (!m) return null;
    return single.outcome === 'HOME' ? m.homeOdds : single.outcome === 'DRAW' ? m.drawOdds : m.awayOdds;
  }, [single.matchId, single.outcome, matches]);

  const oddsForOutcome = (match, outcome) => {
    if (!match) return null;
    if (outcome === 'HOME') return match.homeOdds;
    if (outcome === 'DRAW') return match.drawOdds;
    return match.awayOdds;
  };

  const toggleStatus = (status) => {
    setStatusFilter((prev) =>
      prev.includes(status) ? prev.filter((s) => s !== status) : [...prev, status]
    );
  };

  return (
    <div className="layout">
      <h1>Sports Betting Simulator</h1>
      {error && (
        <div className="toast toast-error">
          <span>{error}</span>
          <button className="secondary" onClick={() => setError('')}>Dismiss</button>
        </div>
      )}
      {message && (
        <div className="toast toast-info">
          <span>{message}</span>
          <button className="secondary" onClick={() => setMessage('')}>Close</button>
        </div>
      )}

      <div className="card">
        <h2>Player</h2>
        <div className="row">
          <div>
            <label>Player name</label>
            <input value={playerName} onChange={(e) => setPlayerName(e.target.value)} placeholder="alice" />
          </div>
          <div>
            <label>Initial balance</label>
            <input type="number" value={initialBalance} onChange={(e) => setInitialBalance(e.target.value)} />
          </div>
        </div>
        <div className="row">
          <button onClick={addPlayer}>Create player</button>
          <button className="secondary" onClick={loadPlayer}>Switch to player</button>
          {currentPlayer && <div>Active: <strong>{currentPlayer.name}</strong> | Balance: {currentPlayer.balance.toFixed(2)}</div>}
        </div>
      </div>

      <div className="grid-two">
        <div className="card">
          <div className="row" style={{ alignItems: 'center', justifyContent: 'space-between' }}>
            <h2 style={{ margin: 0 }}>Matches</h2>
            <button className="secondary" onClick={refreshMatches}>Refresh</button>
          </div>
          <table>
            <thead>
            <tr>
              <th>Fixture</th>
              <th>Status</th>
              <th>Kickoff</th>
              <th>Ends</th>
              <th>Odds (H / D / A)</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {sortedMatches.map((m) => (
              <tr key={m.id}>
                <td>{m.homeTeam} vs {m.awayTeam}</td>
                <td>{m.status}</td>
                <td>{new Date(m.kickoffAt).toLocaleTimeString()}</td>
                <td>{new Date(m.endsAt).toLocaleTimeString()}</td>
                <td>
                  <div className="odds-row">
                    <span className="odds-tag home">H {m.homeOdds}</span>
                    <span className="odds-tag draw">D {m.drawOdds}</span>
                    <span className="odds-tag away">A {m.awayOdds}</span>
                  </div>
                </td>
                <td>
                  <button className="secondary" onClick={() => setSingle({ ...single, matchId: m.id })}>Select Single</button>
                  <button onClick={() => addSelection(m.id)}>Add to Combo</button>
                </td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>

        <div className="card">
          <h2>Place Bets</h2>
          <div className="subcard">
            <h3>Single Bet</h3>
            <div className="row">
              <div>
                <label>Match</label>
                <select value={single.matchId} onChange={(e) => setSingle({ ...single, matchId: e.target.value })}>
                  <option value="">Select match</option>
                  {sortedMatches.map((m) => (
                    <option key={m.id} value={m.id}>{m.homeTeam} vs {m.awayTeam}</option>
                  ))}
                </select>
              </div>
              <div>
                <label>Outcome</label>
                <select value={single.outcome} onChange={(e) => setSingle({ ...single, outcome: e.target.value })}>
                  {outcomes.map((o) => <option key={o} value={o}>{o}</option>)}
                </select>
              </div>
              <div>
                <label>Stake</label>
                <input type="number" value={single.stake} onChange={(e) => setSingle({ ...single, stake: e.target.value })} />
              </div>
            </div>
            <div className="helper-row">
              <span className="muted">Applied odds: {singleSelectedOdds ? singleSelectedOdds.toFixed(2) : '-'}</span>
            </div>
            <button onClick={submitSingle}>Place Single</button>
          </div>

          <div className="subcard">
            <h3>Combination Bet</h3>
            <div className="row">
              <div>
                <label>Stake</label>
                <input type="number" value={comboStake} onChange={(e) => setComboStake(e.target.value)} />
              </div>
              <div>Selections: {comboSelections.length} | Combined odds: {combinedOdds.toFixed(2)}</div>
            </div>
            {comboSelections.map((sel, idx) => (
              <div className="row" key={idx}>
                <div style={{ flex: 1 }}>{sel.label}</div>
                <div className="muted">@ {(() => {
                  const m = matchById(sel.matchId);
                  const o = oddsForOutcome(m, sel.outcome);
                  return o ? o.toFixed(2) : '-';
                })()}</div>
                <select value={sel.outcome} onChange={(e) => {
                  const val = e.target.value;
                  setComboSelections((prev) => prev.map((s, i) => i === idx ? { ...s, outcome: val } : s));
                }}>
                  {outcomes.map((o) => <option key={o} value={o}>{o}</option>)}
                </select>
                <button className="secondary" onClick={() => removeSelection(idx)}>Remove</button>
              </div>
            ))}
            <button onClick={submitCombination}>Place Combination</button>
          </div>
        </div>

        <div className="card">
          <div className="row" style={{ alignItems: 'center', justifyContent: 'space-between' }}>
            <h2 style={{ margin: 0 }}>Finished Matches</h2>
            <button className="secondary" onClick={refreshFinishedMatches}>Refresh</button>
          </div>
          <table>
            <thead>
            <tr>
              <th>Fixture</th>
              <th>Result</th>
              <th>Settled</th>
              <th>Kickoff</th>
              <th>Ends</th>
            </tr>
            </thead>
            <tbody>
            {finishedMatches.map((m) => (
              <tr key={m.id}>
                <td>{m.homeTeam} vs {m.awayTeam}</td>
                <td>{m.result || '-'}</td>
                <td>{m.settledAt ? new Date(m.settledAt).toLocaleTimeString() : '-'}</td>
                <td>{new Date(m.kickoffAt).toLocaleTimeString()}</td>
                <td>{new Date(m.endsAt).toLocaleTimeString()}</td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="card">
        <h2>Bet History</h2>
        <div className="row" style={{ alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
          <span>Filter by status:</span>
          {statusOptions.map((s) => (
            <button
              key={s}
              type="button"
              className={`chip ${statusFilter.includes(s) ? 'active' : ''}`}
              onClick={() => toggleStatus(s)}
            >
              {s}
            </button>
          ))}
          <button className="secondary" onClick={() => setStatusFilter([])}>Clear</button>
        </div>
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Status</th>
            <th>Stake</th>
            <th>Odds</th>
            <th>Potential</th>
            <th>Selections</th>
          </tr>
          </thead>
          <tbody>
          {bets.map((b) => (
            <tr key={b.id}>
              <td>{b.id.slice(0, 8)}</td>
              <td>{b.type}</td>
              <td>{b.status}</td>
              <td>{b.stake}</td>
              <td>{b.finalOdds.toFixed(2)}</td>
              <td>{b.potentialPayout.toFixed(2)}</td>
              <td>{b.selections.map((s) => `${s.homeTeam} vs ${s.awayTeam} (${s.outcome})`).join('; ')}</td>
            </tr>
          ))}
          </tbody>
        </table>
      </div>

      <div className="card">
        <h2>Transactions</h2>
        <table>
          <thead>
          <tr>
            <th>Time</th>
            <th>Type</th>
            <th>Reason</th>
            <th>Amount</th>
            <th>Balance after</th>
          </tr>
          </thead>
          <tbody>
          {transactions.map((t) => (
            <tr key={t.id}>
              <td>{new Date(t.createdAt).toLocaleTimeString()}</td>
              <td>{t.type}</td>
              <td>{t.reason}</td>
              <td>{t.amount}</td>
              <td>{t.balanceAfter}</td>
            </tr>
          ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

