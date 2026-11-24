import {useEffect,useState} from 'react'
import {citizenApi,grievanceApi} from '../api/api.js'
import {ErrorBox,Status,Sla} from './Shared.jsx'
const blank={citizenId:'',category:'WATER_SUPPLY',severity:'HIGH',title:'',description:'',location:''}
export default function Grievances({focusId}){
 const [meta,setMeta]=useState(null),[citizens,setCitizens]=useState([]),[rows,setRows]=useState([]),[filter,setFilter]=useState(''),[form,setForm]=useState(blank),[selected,setSelected]=useState(focusId),[error,setError]=useState(null),[detail,setDetail]=useState(null),[history,setHistory]=useState([])
 const load=()=>grievanceApi.list(filter).then(setRows).catch(setError)
 useEffect(()=>{grievanceApi.meta().then(setMeta).catch(setError);citizenApi.list().then(setCitizens).catch(setError)},[])
 useEffect(()=>{load()},[filter])
 useEffect(()=>{if(focusId)setSelected(focusId)},[focusId])
 const loadDetail=async id=>{setSelected(id);setError(null);try{const [g,h]=await Promise.all([grievanceApi.get(id),grievanceApi.history(id)]);setDetail(g);setHistory(h)}catch(e){setError(e)}}
 useEffect(()=>{if(selected)loadDetail(selected)},[selected])
 const change=e=>setForm({...form,[e.target.name]:e.target.value})
 const submit=async e=>{e.preventDefault();setError(null);try{const g=await grievanceApi.register({...form,citizenId:Number(form.citizenId)});setForm(blank);await load();setSelected(g.id)}catch(err){setError(err)}}
 const run=async fn=>{setError(null);try{await fn();await load();if(selected)await loadDetail(selected)}catch(e){setError(e)}}
 return <><div className="page-title"><div><small>CITIZEN SERVICES</small><h1>Grievance Registration & Redressal</h1></div></div>
 <form className="panel form-grid" onSubmit={submit}>
  <label>Citizen<select name="citizenId" value={form.citizenId} onChange={change} required><option value="">Select citizen</option>{citizens.map(c=><option key={c.id} value={c.id}>{c.fullName} ({c.citizenCode})</option>)}</select></label>
  <label>Category<select name="category" value={form.category} onChange={change}>{meta?.categories?.map(x=><option key={x}>{x}</option>)}</select></label>
  <label>Severity<select name="severity" value={form.severity} onChange={change}>{meta?.severities?.map(x=><option key={x}>{x}</option>)}</select></label>
  <label>Location<input name="location" value={form.location} onChange={change}/></label>
  <label className="wide">Title<input name="title" value={form.title} onChange={change} required/></label>
  <label className="wide">Description<textarea name="description" rows="3" value={form.description} onChange={change} required/></label>
  <div className="wide"><button>Register Grievance</button></div>
 </form><ErrorBox error={error}/>
 <section className="panel"><div className="panel-head"><h2>Grievances ({rows.length})</h2><select value={filter} onChange={e=>setFilter(e.target.value)}><option value="">All statuses</option>{meta?.statuses?.map(x=><option key={x}>{x}</option>)}</select></div>
 <div className="table-wrap"><table><thead><tr><th>Number</th><th>Citizen</th><th>Category</th><th>Department</th><th>Status</th><th>Level</th><th>SLA</th></tr></thead><tbody>{rows.map(g=><tr key={g.id} className={`clickable ${selected===g.id?'selected':''}`} onClick={()=>setSelected(g.id)}><td>{g.grievanceNumber}</td><td>{g.citizenName}</td><td>{g.category}</td><td>{g.departmentName}</td><td><Status value={g.status}/></td><td>L{g.escalationLevel}</td><td><Sla value={g.slaStatus}/></td></tr>)}</tbody></table></div></section>
 {detail&&<Workflow g={detail} history={history} departments={meta?.departments||[]} onRun={run}/>}
 </>
}
function Workflow({g,history,departments,onRun}){
 const [officer,setOfficer]=useState(''),[dept,setDept]=useState(''),[notes,setNotes]=useState('')
 const s=g.status
 return <section className="panel"><div className="panel-head"><h2>{g.grievanceNumber} · Workflow</h2><Status value={s}/></div>
 <h3>{g.title}</h3><p>{g.description}</p><p>Citizen: {g.citizenName} ({g.citizenCode}) · Ward {g.wardNumber}</p>
 <p>Department: {g.departmentName} · Officer: {g.assignedOfficer||'Not assigned'}</p>
 <p>SLA: {g.slaHours}h · Due {new Date(g.slaDueAt).toLocaleString()} · <Sla value={g.slaStatus}/> · {g.hoursRemaining} hours remaining</p>
 <p>Escalation level: {g.escalationLevel}</p>
 {(s==='REGISTERED'||s==='ASSIGNED')&&<div className="inline wrap"><input placeholder="Officer name" value={officer} onChange={e=>setOfficer(e.target.value)}/><select value={dept} onChange={e=>setDept(e.target.value)}><option value="">Keep department</option>{departments.map(d=><option key={d}>{d}</option>)}</select><button onClick={()=>onRun(()=>grievanceApi.assign(g.id,{officerName:officer,department:dept||null}))}>Assign / Re-route</button></div>}
 <div className="inline wrap">
 {s==='ASSIGNED'&&<button onClick={()=>onRun(()=>grievanceApi.status(g.id,{status:'IN_PROGRESS',remarks:'Work started'}))}>Start Work</button>}
 {s==='RESOLVED'&&<button onClick={()=>onRun(()=>grievanceApi.status(g.id,{status:'CLOSED',remarks:'Citizen confirmed'}))}>Close</button>}
 {s==='RESOLVED'&&<button className="secondary" onClick={()=>onRun(()=>grievanceApi.status(g.id,{status:'IN_PROGRESS',remarks:'Reopened by citizen'}))}>Reopen</button>}
 {['REGISTERED','ASSIGNED','IN_PROGRESS'].includes(s)&&<><button className="warn" onClick={()=>onRun(()=>grievanceApi.escalate(g.id,'Manual escalation'))}>Escalate</button><button className="secondary" onClick={()=>onRun(()=>grievanceApi.simulateBreach(g.id))}>Simulate SLA breach (demo)</button></>}
 </div>
 {['REGISTERED','ASSIGNED','IN_PROGRESS'].includes(s)&&<div className="inline wrap"><input className="grow" placeholder={s==='IN_PROGRESS'?'Resolution notes (min 10 chars)':'Rejection reason'} value={notes} onChange={e=>setNotes(e.target.value)}/>{s==='IN_PROGRESS'?<button onClick={()=>onRun(()=>grievanceApi.status(g.id,{status:'RESOLVED',resolutionNotes:notes}))}>Resolve</button>:<button className="danger" onClick={()=>onRun(()=>grievanceApi.status(g.id,{status:'REJECTED',remarks:notes}))}>Reject</button>}</div>}
 <h3 className="timeline-title">Audit timeline</h3><ol className="timeline">{history.map(h=><li key={h.id}><small>{new Date(h.at).toLocaleString()}</small><b>{h.action}</b><span>{h.fromStatus?`${h.fromStatus} → `:''}{h.toStatus||''} {h.remarks||''}</span><small>by {h.performedBy}</small></li>)}</ol>
 </section>
}
