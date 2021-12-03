import React from 'react';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Divider from '@mui/material/Divider';
import AddIcon from '@mui/icons-material/Add';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import CheckIcon from '@mui/icons-material/Check';
import EditIcon from '@mui/icons-material/Edit';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import {newReport, getReportByDeptName,
    addEmptyQuestion, addQuestion, updateQuestion, deleteQuestion, answerQuestion,
    addMultipleChoiceQuestion, updateMultipleChoiceQuestion, updateMultipleChoiceQuestionChoice,
    deleteMultipleChoiceQuestion, answerMultipleChoiceQuestion, addWrittenQuestion,
    updateWrittenQuestion, deleteWrittenQuestion, answerWrittenQuestion} from '../../API/reports';

enum RecordType {
    written = "Written",
    MCQ = "Multiple Choice",
    numerical = "Numerical",
}

type Props = {
    id: number;
    type: RecordType;
    question: string;
    answer: string;
    options: Map<string, string>;
};

type EntryState = {
    id: number;
    question: string;
    answer: string;
    type: RecordType;
    entryField: any[];
    options: string[];
    isEdit: boolean;
};

type RecordState = {
    id: number;
    entryList: RecordEntry[];
};

let reportId: number;

class RecordEntry extends React.Component<Props, EntryState> {
    state: EntryState;

    constructor(props: Props) {
        super(props);
        this.state = {
            id: props.id,
            question: props.question,
            answer: props.answer,
            type: props.type,
            entryField: [],
            options: [],
            isEdit: false,
        };
        if (props.options.size > 0) {
            this.state.options = Array.from(props.options.values());
        }
        switch (props.type) {
            case RecordType.numerical:
                this.state.entryField = this.numericalQuestion();
                break;
            case RecordType.written:
                this.state.entryField = this.writtenQuestion();
                break;
            case RecordType.MCQ:
                this.state.entryField = this.mcq();
                break;
        }
    }

    update() {
        switch (this.state.type) {
            case RecordType.numerical:
                updateQuestion(this.state.id, this.state.question);
                break;
            case RecordType.written:
                updateWrittenQuestion(this.state.id, this.state.question);
                break;
            case RecordType.MCQ:
                updateMultipleChoiceQuestion(this.state.id, this.state.question);
                break;
        }
    }

    updateAnswer() {
        switch (this.state.type) {
            case RecordType.numerical:
                answerQuestion(this.state.id, this.state.answer);
                break;
            case RecordType.written:
                answerWrittenQuestion(this.state.id, this.state.answer);
                break;
            case RecordType.MCQ:
                answerMultipleChoiceQuestion(this.state.id, this.state.answer);
                break;
        }
    }

    delete() {
        switch (this.state.type) {
            case RecordType.numerical:
                deleteQuestion(this.state.id);
                break;
            case RecordType.written:
                deleteWrittenQuestion(this.state.id);
                break;
            case RecordType.MCQ:
                deleteMultipleChoiceQuestion(this.state.id);
                break;
        }
    }

    setQuestionAnswer(question: string, answer: string) {
        this.setState({question: question, answer: answer});
    }

    changeType(event: SelectChangeEvent) {
        if (this.state.type === event.target.value) {
            return
        }
        this.delete();
        switch (event.target.value) {
            case RecordType.MCQ:
                this.setState({type: RecordType.MCQ, answer: ""});
                addMultipleChoiceQuestion(reportId, this.state.question).then((r: any) => {
                    this.setState({id: r.id, entryField: this.mcq()});
                });
                break;
            case RecordType.written:
                this.setState({type: RecordType.written, answer: ""});
                addWrittenQuestion(reportId, this.state.question).then((r: any) => {
                    this.setState({id: r.id, entryField: this.writtenQuestion()});
                });
                break;
            case RecordType.numerical:
                this.setState({type: RecordType.written, answer: ""});
                addQuestion(reportId, this.state.question).then((r: any) => {
                    this.setState({id: r.id, entryField: this.numericalQuestion()});
                });
        }
    }

    numericalQuestion() {
        let entryList: any[] = [(
            <Grid item xs={8}>
                <TextField fullWidth id="field" label="Field" variant="outlined"
                           defaultValue={this.state.question} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                    this.setState({question: event.target.value}, () => {
                        this.update();
                    });
                }}/>
            </Grid>
        )];
        entryList.push(
            <Grid item xs={2}>
                <TextField fullWidth id="value" label="Value" variant="outlined" type="number"
                           defaultValue={this.state.answer} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                    this.setState({answer: event.target.value}, () => {
                        this.updateAnswer();
                    });
                }}/>
            </Grid>
        )
        return entryList;
    }

    writtenQuestion() {
        let entryList: any[] = [(
            <Grid item xs={10}>
                <TextField fullWidth id="field" label="Field" variant="outlined"
                           defaultValue={this.state.question} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                    this.setState({question: event.target.value}, () => {
                        this.update();
                    });
                }}/>
            </Grid>
        ), (
            <Grid item xs={12}>
                <TextField fullWidth id="value" label="Value" variant="outlined"
                           defaultValue={this.state.answer} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                    this.setState({answer: event.target.value}, () => {
                        this.updateAnswer();
                    });
                }}/>
            </Grid>
        )];
        return entryList;
    }

    mcq() {
        if (this.state.options.length === 0) {
            this.setState({isEdit: true});
        }
        let entryList: any[] = [(
            <Grid item xs={10}>
                <TextField fullWidth id="field" label="Field" variant="outlined"
                           defaultValue={this.state.question} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                    this.setState({question: event.target.value}, () => {
                        this.update();
                    });
                }}/>
            </Grid>
        )];
        if (this.state.isEdit) {
            this.state.options.forEach((value, index) => {
                entryList.push(
                    <Grid item xs={4}>
                        <TextField fullWidth id="choices" label={String.fromCharCode(65 + index)} variant="standard"
                                   InputProps={{endAdornment: (
                                       <InputAdornment position="end">
                                           <IconButton onClick={event => {
                                               this.setState({options: this.state.options.splice(index, 1)}, () => {
                                                   this.setState({entryField: this.mcq()});
                                                   updateMultipleChoiceQuestionChoice(this.state.id, this.getOptionMap());
                                               })
                                           }}>
                                               <DeleteForeverIcon />
                                           </IconButton>
                                       </InputAdornment>)}}
                                   defaultValue={value} onChange={event => {
                            this.setState({options: this.state.options.splice(index, 1, event.target.value)}, () => {
                                updateMultipleChoiceQuestionChoice(this.state.id, this.getOptionMap());
                            })
                        }}/>
                    </Grid>
                )
            });
            entryList.push(
                <Grid item xs={4}>
                    <IconButton onClick={event => {
                        this.setState({options: [...this.state.options, ""]}, () => {
                            this.setState({entryField: this.mcq()});
                            updateMultipleChoiceQuestionChoice(this.state.id, this.getOptionMap());
                        });
                    }}>
                        <AddIcon />
                    </IconButton>
                    <IconButton onClick={event => {
                        this.setState({isEdit: false}, () => {
                            this.setState({entryField: this.mcq()});
                        });
                    }}>
                        <CheckIcon />
                    </IconButton>
                </Grid>
            );
        } else {
            entryList.push(
                <Grid item xs={12}>
                    <FormControl component="fieldset">
                        <RadioGroup row name="mcq-options" defaultValue={this.state.answer} onChange={event => {
                            this.setState({answer: event.target.value}, () => {
                                this.updateAnswer();
                            });
                        }}>
                            {this.state.options.map((value, index) => {
                                return (<FormControlLabel value={String.fromCharCode(65 + index)} control={<Radio />} label={value} />)
                            })}
                            <IconButton onClick={() => {
                                this.setState({isEdit: true}, () => {
                                    this.setState({entryField: this.mcq()});
                                });
                            }}>
                                <EditIcon />
                            </IconButton>
                        </RadioGroup>
                    </FormControl>
                </Grid>
            );
        }
        return entryList;
    }

    getOptionMap() {
        let optionMap = new Map<string, string>();
        this.state.options.forEach((value, index) => {
            optionMap.set(String.fromCharCode(65 + index), value);
        })
        return optionMap;
    }

    render() {
        return (
            <div>
                <ListItem alignItems="flex-start">
                    <Grid container spacing={2}>
                        <Grid item xs={2}>
                            <FormControl fullWidth>
                                <InputLabel id="question-type-select-label">Type</InputLabel>
                                <Select
                                    labelId="question-type-select-label"
                                    id="question-type"
                                    value={this.state.type}
                                    label="Type"
                                    onChange={(event) => {this.changeType(event)}}
                                >
                                    <MenuItem value={RecordType.numerical}>Numerical</MenuItem>
                                    <MenuItem value={RecordType.written}>Written</MenuItem>
                                    <MenuItem value={RecordType.MCQ}>Multiple Choice</MenuItem>
                                </Select>
                            </FormControl>
                        </Grid>
                        {this.state.entryField}
                    </Grid>
                </ListItem>
                <Divider component="li" variant='middle'/>
            </div>
        );
    }
}

class DataInput extends React.Component<any, any> {
    state: RecordState = {
        entryList: [],
        id: 0,
    };

    constructor(props: any) {
        super(props)
        const name = this.props.location.state.name || null
        newReport(name).then((r: any) => {
                this.setState({id: r.id});
                reportId = r.id;
            }
        ).catch(error => {
            console.log(error.message);
            getReportByDeptName(name).then((r: any) => {
                let questionList: any[] = [...r.numericalQuestions, ...r.writtenQuestions, ...r.multipleChoiceQuestions];
                questionList.sort(this.compareQuestion)
                let entryList: any[] = [];
                questionList.forEach(e => {
                    if (r.numericalQuestions.includes(e)) {
                        let stringAns: string = "";
                        if (e.answer != null) {
                            stringAns = e.answer.toString();
                        }
                        entryList = [...entryList, this.existEntry(e.id, RecordType.numerical, e.question, stringAns)]
                    } else if (r.writtenQuestions.includes(e)) {
                        entryList = [...entryList, this.existEntry(e.id, RecordType.written, e.question, e.answer)]
                    } else if (r.multipleChoiceQuestions.includes(e)) {
                        entryList = [...entryList, this.existEntry(e.id, RecordType.MCQ, e.question, e.choice, e.choices)]
                    }
                })
                this.setState({id: r.id, entryList: entryList});
                reportId = r.id;
        })})
    }

    private compareQuestion(a: { id: number; }, b: { id: number; }) {
        return a.id - b.id;
    }

    newEntry(id: number) {
        return <RecordEntry id={id} type={RecordType.numerical} question={""} answer={""} options={new Map<string, string>()}/>
    }

    existEntry(id: number, type: RecordType, question: string, answer: string, options: Map<string, string> = new Map<string, string>()) {
        return <RecordEntry id={id} type={type} question={question} answer={answer} options={options}/>
    }

    createNewEntry() {
        addEmptyQuestion(this.state.id).then((r: any) => {
            let entry: any = this.newEntry(r.id);
            this.setState({entryList: [...this.state.entryList, entry]});
        })
    }

    render() {
      const name = this.props.location.state.name || null
        return (
            <div className="DataInput">
                <h1 style={{marginLeft: "1em", marginRight: "1em"}}>{ name } Department Data Input</h1>
                <List style={{marginLeft: "1em", marginRight: "1em"}}>
                    {this.state.entryList}
                    <ListItem>
                        <Button variant="contained" size="large" startIcon={<AddIcon fontSize="large"/>} onClick={() => {
                            this.createNewEntry();
                        }}>Add</Button>
                    </ListItem>
                </List>
            </div>
        )
    }
}

export default DataInput;
